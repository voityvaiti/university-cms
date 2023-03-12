package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.model.Group;
import org.foxminded.rymarovych.service.abstractions.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

@WebMvcTest(GroupController.class)
class GroupControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GroupService groupService;



    @Test
    void getList() throws Exception {

        List<Group> expected = new ArrayList<>();
        expected.add(new Group("GH-103", "Musical art", 3));

        when(groupService.getAllGroupsList()).thenReturn(expected);

        this.mvc.perform(get("/groups/get-list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attribute("groups", expected));

        verify(groupService).getAllGroupsList();
    }
}