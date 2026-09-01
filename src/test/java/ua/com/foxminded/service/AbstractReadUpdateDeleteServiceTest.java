package ua.com.foxminded.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.persistence.EntityNotFoundException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.com.foxminded.MainApp;
import ua.com.foxminded.service.suppport.PostgresTestContainer;
import ua.com.foxminded.service.suppport.TestDataFactory;

@Testcontainers
@Transactional
@SpringBootTest(classes = { MainApp.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({ "test" })
public abstract class AbstractReadUpdateDeleteServiceTest<E, D, I> {
    
    @Autowired
    protected TestDataFactory testDataFactory;
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        PostgreSQLContainer<?> container = PostgresTestContainer.getInstance();
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
    
    protected abstract ReadUpdateDeleteService<E, D, I> getService();
    protected abstract E persistValidEntity();
    protected abstract I getId(E entity);
    protected abstract I nonExistentId();

    
    @Test
    void findById_shouldFindEntityById_whenEntityExists() {
        E saved = persistValidEntity();

        E found = getService().findById(getId(saved));

        assertNotNull(found);
        assertEquals(getId(saved), getId(found));
    }

    @Test
    void findById_shouldThrowException_whenEntityDoesNotExist() {
        assertThrows(EntityNotFoundException.class,
                () -> getService().findById(nonExistentId()));
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoEntities() {
        assertTrue(getService().findAll().isEmpty());
    }

    @Test
    void findAll_shouldReturnAllSavedEntities() {
        persistValidEntity();
        persistValidEntity();

        assertEquals(2, getService().findAll().size());
    }

    @Test
    void deleteById_shouldRemoveEntity_whenEntityExists() {
        E saved = persistValidEntity();
        I id = getId(saved);

        getService().deleteById(id);

        assertThrows(EntityNotFoundException.class, () -> getService().findById(id));
    }
}
