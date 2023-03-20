package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.model.Group;
import org.foxminded.rymarovych.model.Student;
import org.foxminded.rymarovych.service.abstractions.StudentService;
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

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
class StudentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StudentService studentService;


    @Test
    void getList() throws Exception {

        List<Student> expected = new ArrayList<>();

        Student student = new Student(1L, "Some", "Student", new Group());
        student.setFirstName("Some");
        student.setLastName("Students");
        student.setGroup(new Group());

        expected.add(student);

        when(studentService.getAllStudentsList()).thenReturn(expected);

        this.mvc.perform(get("/students/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attribute("students", expected));

        verify(studentService).getAllStudentsList();
    }
}