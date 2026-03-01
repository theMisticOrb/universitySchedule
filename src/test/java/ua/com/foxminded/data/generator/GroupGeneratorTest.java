package ua.com.foxminded.data.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.service.GroupService;

@ExtendWith(MockitoExtension.class)
class GroupGeneratorTest {

    @Mock
    private GroupService repository;

    private GroupGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new GroupGenerator(repository);
    }

    @Test
    void generateGroups_shouldGenerateRequestedNumberOfGroups_whenCalled() {

        Group group = new Group();
        group.setId(1);

        Specialization specialization = new Specialization();
        specialization.setId(1);

        when(repository.addAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        int groupsCount = 30;
        List<Group> result = generator.generateGroups(groupsCount, specialization);

        assertEquals(groupsCount, result.size());
        verify(repository).addAll(any());
    }

}
