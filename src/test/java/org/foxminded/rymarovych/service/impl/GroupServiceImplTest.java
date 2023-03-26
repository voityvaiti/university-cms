package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.CourseRepository;
import org.foxminded.rymarovych.dao.GroupRepository;
import org.foxminded.rymarovych.model.Course;
import org.foxminded.rymarovych.model.Group;
import org.foxminded.rymarovych.service.abstractions.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = GroupServiceImpl.class)
class GroupServiceImplTest {

    @MockBean
    GroupRepository groupRepository;
    @MockBean
    CourseRepository courseRepository;

    @Autowired
    GroupService groupService;

    @Test
    void getUnlinkedCourses() {
        Long id = 9L;

        Course course1 = new Course(1L, "name1", null, null, null);
        Course course2 = new Course(2L, "name2", null, null, null);
        Course course3 = new Course(3L, "name3", null, null, null);
        Course course4 = new Course(4L, "name4", null, null, null);

        Group group = new Group(id, "name", "spec", 3, null, new HashSet<>(List.of(course3, course4)), null);

        List<Course> expected = List.of(course1, course2);

        when(groupRepository.findById(id)).thenReturn(Optional.of(group));
        when(courseRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(course1, course2, course3, course4)));

        assertEquals(expected, groupService.getUnlinkedCourses(id));

    }

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