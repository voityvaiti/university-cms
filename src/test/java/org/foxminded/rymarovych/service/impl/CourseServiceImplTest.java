package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.CourseRepository;
import org.foxminded.rymarovych.dao.GroupRepository;
import org.foxminded.rymarovych.dao.TeacherRepository;
import org.foxminded.rymarovych.model.Course;
import org.foxminded.rymarovych.model.Group;
import org.foxminded.rymarovych.model.Teacher;
import org.foxminded.rymarovych.service.abstractions.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = CourseServiceImpl.class)
class CourseServiceImplTest {

    @MockBean
    CourseRepository courseRepository;
    @MockBean
    GroupRepository groupRepository;
    @MockBean
    TeacherRepository teacherRepository;

    @Autowired
    CourseService courseService;


    @Test
    void getUnlinkedGroups() {
        Long id = 4L;

        Group group1 = new Group(1L, "name1", "spec1", 1, null, null, null);
        Group group2 = new Group(2L, "name2", "spec2", 2, null, null, null);
        Group group3 = new Group(3L, "name3", "spec3", 3, null, null, null);
        Group group4 = new Group(4L, "name4", "spec4", 4, null, null, null);

        Course course = new Course(id, "name", null, new HashSet<>(List.of(group2, group4)), null);

        List<Group> expected = List.of(group1, group3);

        when(courseRepository.findById(id)).thenReturn(Optional.of(course));
        when(groupRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(group1, group2, group3, group4)));

        assertEquals(expected, courseService.getUnlinkedGroups(id));

    }

    @Test
    void getUnlinkedTeachers() {
        Long id = 4L;

        Teacher teacher1 = new Teacher(1L, "firstname1", "lastname1", "degree1", null, null);
        Teacher teacher2 = new Teacher(2L, "firstname2", "lastname2", "degree2", null, null);
        Teacher teacher3 = new Teacher(3L, "firstname3", "lastname3", "degree3", null, null);
        Teacher teacher4 = new Teacher(4L, "firstname4", "lastname4", "degree4", null, null);

        Course course = new Course(id, "name", null, null, new HashSet<>(List.of(teacher1, teacher4)));

        List<Teacher> expected = List.of(teacher2, teacher3);

        when(courseRepository.findById(id)).thenReturn(Optional.of(course));
        when(teacherRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(teacher1, teacher2, teacher3, teacher4)));

        assertEquals(expected, courseService.getUnlinkedTeachers(id));

    }

    @Test
    void updateIfCourseFound() {
        Long id = 4L;
        Course currentCourse = new Course(id, "Math", null, null, null);
        Course updatedCourse = new Course(id, "Biology", null, null, null);

        when(courseRepository.findById(id)).thenReturn(Optional.of(currentCourse));
        when(courseRepository.save(any())).then(returnsFirstArg());

        assertEquals(updatedCourse, courseService.update(id, updatedCourse));

        verify(courseRepository).save(updatedCourse);

    }

    @Test
    void notUpdateIfCourseNotFound() {
        Long id = 4L;

        Course updatedCourse = new Course(id, "Biology", null, null, null);

        when(courseRepository.findById(id)).thenReturn(Optional.empty());

        assertNotEquals(updatedCourse, courseService.update(id, updatedCourse));

        verify(courseRepository, never()).save(any());

    }

}