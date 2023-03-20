package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.model.Teacher;
import org.foxminded.rymarovych.service.abstractions.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeacherController.class)
@AutoConfigureMockMvc(addFilters = false)
class TeacherControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TeacherService teacherService;


    @Test
    void getList() throws Exception {

        List<Teacher> expected = new ArrayList<>();

        Teacher teacher = new Teacher(1L, "Some", "Teacher",
                "Doctor", null, null);

        expected.add(teacher);


        when(teacherService.getAllTeachersList()).thenReturn(expected);

        this.mvc.perform(get("/teachers/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("teachers"))
                .andExpect(model().attribute("teachers", expected));

        verify(teacherService).getAllTeachersList();
    }
}