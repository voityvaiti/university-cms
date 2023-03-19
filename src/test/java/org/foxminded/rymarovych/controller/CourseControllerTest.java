package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.model.Course;
import org.foxminded.rymarovych.service.abstractions.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.List;

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
        expected.add(new Course("Math"));

        when(courseService.getAllCoursesList()).thenReturn(expected);

        this.mvc.perform(get("/courses/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attribute("courses", expected));

        verify(courseService).getAllCoursesList();
    }
}