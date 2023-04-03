package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.model.Group;
import org.foxminded.rymarovych.model.Student;
import org.foxminded.rymarovych.service.abstractions.StudentService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
class StudentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StudentService studentService;

    private static Student student;

    @BeforeAll
    static void setUp() {
        Group group = new Group(5L, "groupname", "spec", 3, null, null, null);

        student = new Student(1L, "Some", "Student", group);
    }


    @Test
    void getList() throws Exception {

        List<Student> expected = new ArrayList<>();

        expected.add(student);

        when(studentService.getAllStudentsList()).thenReturn(expected);

        this.mvc.perform(get("/students/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attribute("students", expected));

        verify(studentService).getAllStudentsList();
    }

    @Test
    void create_ShouldAddStudentAndLinkGroup_IfGroupIdIsPresent() throws Exception {

        this.mvc.perform(post("/students/new")
                .flashAttr("student", student));


        verify(studentService).add(student);
    }

    @Test
    void create_shouldAddStudentAndNotLinkGroup_ifGroupIdIsAbsent() throws Exception {

        this.mvc.perform(post("/students/new")
                .flashAttr("student", student));


        verify(studentService).add(student);
    }

    @Test
    void edit_shouldSendStudentAndAllGroups_ifStudentIsPresent() throws Exception {

        Group group1 = new Group(1L, "name1", "spec1", 1, null, null, null);
        Group group2 = new Group(2L, "name2", "spec2", 2, null, null, null);
        Group group3 = new Group(3L, "name3", "spec3", 3, null, null, null);

        List<Group> groups = Arrays.asList(group1, group2, group3);

        when(studentService.findById(student.getId())).thenReturn(Optional.of(student));
        when(studentService.getAllGroups()).thenReturn(groups);

        mvc.perform(get("/students/edit/" + student.getId()))
                .andExpect(model().attributeExists("student"))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attribute("student", student))
                .andExpect(model().attribute("groups", groups));

        verify(studentService).findById(student.getId());
        verify(studentService).getAllGroups();
    }

    @Test
    void edit_shouldSendErrorMessage_ifStudentIsAbsent() throws Exception {
        Long id = 4L;

        when(studentService.findById(any())).thenReturn(Optional.empty());

        mvc.perform(get("/students/edit/" + id))
                .andExpect(model().attributeExists("errorMessage"));

        verify(studentService).findById(id);
        verify(studentService, never()).getAllGroups();
    }

    @Test
    void update_shouldUpdateAndLinkGroup_ifGroupIdIsPresent() throws Exception {
        Long groupId = 7L;

        mvc.perform(post("/students/edit/" + student.getId())
                .flashAttr("student", student)
                .param("groupId", String.valueOf(groupId)));

        verify(studentService).update(student.getId(), student);
    }

    @Test
    void update_shouldUpdateAndNotLinkGroup_ifGroupIdIsAbsent() throws Exception {

        mvc.perform(post("/students/edit/" + student.getId())
                .flashAttr("student", student));

        verify(studentService).update(student.getId(), student);
    }
}