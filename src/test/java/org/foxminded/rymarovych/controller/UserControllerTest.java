package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.model.Role;
import org.foxminded.rymarovych.model.User;
import org.foxminded.rymarovych.service.abstractions.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Optional;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    void createIfUsernameIsUnique() throws Exception {
        User createdUser = new User(5L, "username", "password", true, Set.of(Role.STUDENT, Role.TEACHER));

        when(userService.findByUsername(createdUser.getUsername())).thenReturn(Optional.empty());

        mvc.perform(post("/users")
                .flashAttr("user", createdUser));

        verify(userService).findByUsername(createdUser.getUsername());
        verify(userService).add(createdUser);
    }

    @Test
    void createIfUsernameIsDuplicated() throws Exception {
        User createdUser = new User(5L, "username", "password", true, Set.of(Role.STUDENT, Role.TEACHER));

        when(userService.findByUsername(createdUser.getUsername())).thenReturn(Optional.of(new User()));

        mvc.perform(post("/users")
                .flashAttr("user", createdUser))
                .andExpect(model().attributeExists("errorMessage"));

        verify(userService).findByUsername(createdUser.getUsername());
        verify(userService, never()).add(any());
    }

    @Test
    void editIfUserIsPresent() throws Exception {

        User user = new User(5L, "username", "password", true, Set.of(Role.STUDENT, Role.TEACHER));

        when(userService.findById(user.getId())).thenReturn(Optional.of(user));

        mvc.perform(get("/users/edit/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", user));

        verify(userService).findById(user.getId());
    }

    @Test
    void editIfUserIsAbsent() throws Exception {

        when(userService.findById(any())).thenReturn(Optional.empty());

        mvc.perform(get("/users/edit/10"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMessage"));

        verify(userService).findById(any());
    }

    @Test
    void updateIfUsernameIsUnique() throws Exception {
        User updatedUser = new User(5L, "username", "password", true, Set.of(Role.STUDENT, Role.TEACHER));

        when(userService.findByUsername("username")).thenReturn(Optional.of(updatedUser));

        mvc.perform(post("/users/edit/" + updatedUser.getId())
                .flashAttr("user", updatedUser));

        verify(userService).findByUsername(updatedUser.getUsername());
        verify(userService).update(updatedUser.getId(), updatedUser);

    }

    @Test
    void updateIfUsernameIsDuplicated() throws Exception {

        User updatedUser = new User(5L, "username", "password", true, Set.of(Role.STUDENT, Role.TEACHER));
        User userWithSameUsername = new User(8L, "username", "anotherPassword", false, Set.of(Role.TEACHER));

        when(userService.findByUsername(any())).thenReturn(Optional.of(userWithSameUsername));

        mvc.perform(post("/users/edit/" + updatedUser.getId())
                .flashAttr("user", updatedUser))
                .andExpect(model().attributeExists("errorMessage"));

        verify(userService).findByUsername(updatedUser.getUsername());
        verify(userService, never()).update(any(Long.class), any(User.class));

    }
}