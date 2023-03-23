package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.model.Course;
import org.foxminded.rymarovych.service.abstractions.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
@AutoConfigureMockMvc(addFilters = false)
class CourseControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CourseService courseService;


    @Test
    void getList() throws Exception {

        List<Course> expected = new ArrayList<>();

        Course course = new Course(1L, "Math", null, null, null);

        expected.add(course);

        when(courseService.getAllCoursesList()).thenReturn(expected);

        this.mvc.perform(get("/courses/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attribute("courses", expected));

        verify(courseService).getAllCoursesList();
    }

    @Test
    void editIfCourseIsPresent() throws Exception {
        Course course = new Course(4L, "coursename", null, null, null);

        when(courseService.findById(course.getId())).thenReturn(Optional.of(course));

        mvc.perform(get("/courses/edit/" + course.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attribute("course", course));

        verify(courseService).findById(course.getId());
    }

    @Test
    void editIfCourseIsAbsent() throws Exception {
        Long id = 10L;

        when(courseService.findById(any())).thenReturn(Optional.empty());

        mvc.perform(get("/courses/edit/" + id))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMessage"));

        verify(courseService).findById(id);
    }
}