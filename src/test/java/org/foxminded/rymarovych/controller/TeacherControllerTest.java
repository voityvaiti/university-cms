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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    void editIfTeacherIsPresent() throws Exception {
        Teacher teacher = new Teacher(4L, "firstname", "lastname",
                "doctor", null, null);

        when(teacherService.findById(teacher.getId())).thenReturn(Optional.of(teacher));

        mvc.perform(get("/teachers/edit/" + teacher.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("teacher"))
                .andExpect(model().attribute("teacher", teacher));

        verify(teacherService).findById(teacher.getId());
    }

    @Test
    void editIfGroupIsAbsent() throws Exception {
        Long id = 3L;

        when(teacherService.findById(any())).thenReturn(Optional.empty());

        mvc.perform(get("/teachers/edit/" + id))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMessage"));

        verify(teacherService).findById(id);
    }

    @Test
    void linkCourseIfActionIsLink() throws Exception {
        String action = "link";

        String teacherId = "4";
        String courseId = "8";

        mvc.perform(post("/teachers/course-relation/" + action)
                .param("teacherId", teacherId)
                .param("courseId", courseId));

        verify(teacherService).linkCourse(Long.parseLong(teacherId), Long.parseLong(courseId));
    }

    @Test
    void unlinkCourseIfActionIsUnlink() throws Exception {
        String action = "unlink";

        String teacherId = "4";
        String courseId = "8";

        mvc.perform(post("/teachers/course-relation/" + action)
                .param("teacherId", teacherId)
                .param("courseId", courseId));

        verify(teacherService).unlinkCourse(Long.parseLong(teacherId), Long.parseLong(courseId));
    }
}