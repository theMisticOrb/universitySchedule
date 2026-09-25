package ua.com.foxminded.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;

public abstract class AbstractFullCrudServiceTest<E, D, I> extends AbstractReadUpdateDeleteServiceTest<E, D, I> {

    @Override
    protected abstract FullCrudService<E, D, I> getService();

    protected abstract D createValidDto();

    @Override
    protected E persistValidEntity() {
        return getService().addOne(createValidDto());
    }

    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {
        E saved = getService().addOne(createValidDto());
        assertNotNull(getId(saved));
    }
}
