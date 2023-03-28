package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.model.Group;
import org.foxminded.rymarovych.service.abstractions.GroupService;
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

@WebMvcTest(GroupController.class)
@AutoConfigureMockMvc(addFilters = false)
class GroupControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GroupService groupService;



    @Test
    void getList() throws Exception {

        List<Group> expected = new ArrayList<>();

        Group group = new Group(5L, "GH-103", "Musical art", 3,
                null, null, null);

        expected.add(group);

        when(groupService.getAllGroupsList()).thenReturn(expected);

        this.mvc.perform(get("/groups/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attribute("groups", expected));

        verify(groupService).getAllGroupsList();
    }

    @Test
    void editIfGroupIsPresent() throws Exception {
        Group group = new Group(4L, "groupname", "spec", 3, null, null, null);

        when(groupService.findById(group.getId())).thenReturn(Optional.of(group));

        mvc.perform(get("/groups/edit/" + group.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("group"))
                .andExpect(model().attribute("group", group));

        verify(groupService).findById(group.getId());
    }

    @Test
    void editIfGroupIsAbsent() throws Exception {
        Long id = 7L;

        when(groupService.findById(any())).thenReturn(Optional.empty());

        mvc.perform(get("/groups/edit/" + id))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMessage"));

        verify(groupService).findById(id);
    }

    @Test
    void linkCourseIfActionIsLink() throws Exception {
        String action = "link";

        String groupId = "7";
        String courseId = "3";

        mvc.perform(post("/groups/course-relation/" + action)
                .param("groupId", groupId)
                .param("courseId", courseId));

        verify(groupService).linkCourse(Long.parseLong(groupId), Long.parseLong(courseId));
    }

    @Test
    void unlinkCourseIfActionIsUnlink() throws Exception {
        String action = "unlink";

        String groupId = "7";
        String courseId = "3";

        mvc.perform(post("/groups/course-relation/" + action)
                .param("groupId", groupId)
                .param("courseId", courseId));

        verify(groupService).unlinkCourse(Long.parseLong(groupId), Long.parseLong(courseId));
    }
}