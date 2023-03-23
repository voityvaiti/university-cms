package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.UserRepository;
import org.foxminded.rymarovych.model.Role;
import org.foxminded.rymarovych.model.User;
import org.foxminded.rymarovych.service.abstractions.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {UserServiceImpl.class})
class UserServiceImplTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    void add() {
        User user = new User();
        user.setPassword("any");

        userService.add(user);

        verify(userRepository).save(any(User.class));

    }

    @Test
    void update() {
        Long id = 5L;
        User currentUser = new User(id, "username", "any", false, Set.of(new Role[]{Role.STUDENT}));
        User updatedUser = new User(id, "newname", "any", true, Set.of(new Role[]{Role.TEACHER}));

        when(userRepository.findById(id)).thenReturn(Optional.of(currentUser));
        when(userRepository.save(any())).then(returnsFirstArg());

        User currentUserUpdatedByService = userService.update(id, updatedUser);

        assertEquals(updatedUser.getId(), currentUserUpdatedByService.getId());
        assertEquals(updatedUser.getUsername(), currentUserUpdatedByService.getUsername());
        assertEquals(updatedUser.isEnabled(), currentUserUpdatedByService.isEnabled());
        assertEquals(updatedUser.getRoles(), currentUserUpdatedByService.getRoles());

        verify(userRepository).save(any(User.class));

    }
}