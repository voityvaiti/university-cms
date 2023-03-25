package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.CourseRepository;
import org.foxminded.rymarovych.model.Course;
import org.foxminded.rymarovych.service.abstractions.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = CourseServiceImpl.class)
class CourseServiceImplTest {

    @MockBean
    CourseRepository courseRepository;

    @Autowired
    CourseService courseService;

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