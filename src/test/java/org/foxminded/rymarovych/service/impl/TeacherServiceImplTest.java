package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.CourseRepository;
import org.foxminded.rymarovych.dao.TeacherRepository;
import org.foxminded.rymarovych.model.Course;
import org.foxminded.rymarovych.model.Teacher;
import org.foxminded.rymarovych.service.abstractions.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TeacherServiceImpl.class)
class TeacherServiceImplTest {

    @MockBean
    TeacherRepository teacherRepository;
    @MockBean
    CourseRepository courseRepository;

    @Autowired
    TeacherService teacherService;

    @Test
    void getUnlinkedCourses() {
        Long id = 8L;

        Course course1 = new Course(1L, "name1", null, null, null);
        Course course2 = new Course(2L, "name2", null, null, null);
        Course course3 = new Course(3L, "name3", null, null, null);
        Course course4 = new Course(4L, "name4", null, null, null);

        Teacher teacher = new Teacher(id, "firstname", "lastname", "degree", null, new HashSet<>(List.of(course1, course4)));

        List<Course> expected = List.of(course2, course3);

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));
        when(courseRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(course1, course2, course3, course4)));

        assertEquals(expected, teacherService.getUnlinkedCourses(id));

    }

    @Test
    void updateIfTeacherFound() {
        Long id = 7L;

        Teacher currnetTeacher = new Teacher(id, "firstname", "lastname", "degree", null, null);
        Teacher updatedTeacher = new Teacher(id, "updatedFirstname", "UpdatedLastname", "UpdatedDegree", null, null);

        when(teacherRepository.findById(id)).thenReturn(Optional.of(currnetTeacher));
        when(teacherRepository.save(any())).then(returnsFirstArg());

        assertEquals(updatedTeacher, teacherService.update(id, updatedTeacher));

        verify(teacherRepository).save(updatedTeacher);

    }

    @Test
    void notUpdateIfTeacherNotFound() {
        Long id = 7L;

        Teacher updatedTeacher = new Teacher(id, "updatedFirstname", "UpdatedLastname", "UpdatedDegree", null, null);

        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        assertNotEquals(updatedTeacher, teacherService.update(id, updatedTeacher));

        verify(teacherRepository, never()).save(any());

    }
}