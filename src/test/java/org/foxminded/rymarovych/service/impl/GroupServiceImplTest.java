package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.GroupRepository;
import org.foxminded.rymarovych.model.Group;
import org.foxminded.rymarovych.service.abstractions.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = GroupServiceImpl.class)
class GroupServiceImplTest {

    @MockBean
    GroupRepository groupRepository;

    @Autowired
    GroupService groupService;

    @Test
    void updateIfGroupFound() {
        Long id = 6L;
        Group currentGroup = new Group(id, "NV-32", "Comp science", 3, null, null, null);
        Group updatedGroup = new Group(id, "CX-82", "Medicine", 2, null, null, null);

        when(groupRepository.findById(id)).thenReturn(Optional.of(currentGroup));
        when(groupRepository.save(any())).then(returnsFirstArg());

        assertEquals(updatedGroup, groupService.update(id, updatedGroup));

        verify(groupRepository).save(updatedGroup);

    }

    @Test
    void notUpdateIfGroupNotFound() {
        Long id = 6L;

        Group updatedGroup = new Group(id, "CX-82", "Medicine", 2, null, null, null);

        when(groupRepository.findById(id)).thenReturn(Optional.empty());

        assertNotEquals(updatedGroup, groupService.update(id, updatedGroup));

        verify(groupRepository, never()).save(any());

    }
}