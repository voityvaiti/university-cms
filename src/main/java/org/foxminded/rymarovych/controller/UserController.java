package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.model.User;
import org.foxminded.rymarovych.service.abstractions.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    static final String USERNAME_DUPLICATION_ERROR_MESSAGE = "User with same username is already exists";

    static final String REQUEST_RECEIVING_LOG_MESSAGE = " HTTP request received";

    static final String REDIRECT_TO_USERS_MENU = "redirect:/users";

    static final String ERROR_MESSAGE_ATTR = "errorMessage";

    static final String USER_ATTR = "user";

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String index() {
        LOGGER.debug("/users GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        return "user/menu";
    }

    @GetMapping("/all")
    public String all(Model model) {
        LOGGER.debug("/users/all GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        model.addAttribute("users", userService.getAllUsersList());
        return "user/list";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        LOGGER.debug("/users/show/{} GET" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        model.addAttribute("optionalUser", userService.findById(id));
        return "user/show";
    }

    @GetMapping("/current")
    public String getCurrent(Model model) {
        LOGGER.debug("/users/current GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        model.addAttribute(USER_ATTR, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "user/show-info";
    }
    @GetMapping("/new")
    public String newUser(Model model) {
        LOGGER.debug("/users/new GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        model.addAttribute(USER_ATTR, new User());
        return "user/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute(USER_ATTR) User user, Model model) {
        LOGGER.debug("/users/new POST" + REQUEST_RECEIVING_LOG_MESSAGE);

        if(userService.findByUsername(user.getUsername()).isPresent()) {
            LOGGER.debug("Received User to create with duplicated username: {} ", user.getUsername());

            model.addAttribute(USER_ATTR, user);
            model.addAttribute(ERROR_MESSAGE_ATTR, USERNAME_DUPLICATION_ERROR_MESSAGE);
            return "user/new";
        }
        userService.add(user);
        LOGGER.debug("User added. Username: {}", user.getUsername());

        return REDIRECT_TO_USERS_MENU;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        LOGGER.debug("/users/edit/{} GET" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        Optional<User> optionalUser = userService.findById(id);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();

            LOGGER.debug("Found user to edit. ID: {}, username: {}", user.getId(), user.getUsername());

            model.addAttribute(USER_ATTR, optionalUser.get());

        } else {
            LOGGER.warn("Not found user to edit by ID: {}", id);

            model.addAttribute(USER_ATTR, new User());
            model.addAttribute(ERROR_MESSAGE_ATTR, "No such user");

        }
        return "user/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@ModelAttribute(USER_ATTR) User user, Model model, @PathVariable("id") Long id) {
        LOGGER.debug("/users/edit/{} POST" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        Optional<User> potentialUserWithSameUsername = userService.findByUsername(user.getUsername());

        if(potentialUserWithSameUsername.isEmpty() || potentialUserWithSameUsername.get().getId().equals(id)) {
            LOGGER.debug("Username duplication of received User is not detected. Updating User: {}", user);

            userService.update(id, user);
            return REDIRECT_TO_USERS_MENU;

        } else {
            LOGGER.debug("Detected username duplication of received User: {}. Update was not executed", user);

            model.addAttribute(USER_ATTR, user);
            model.addAttribute(ERROR_MESSAGE_ATTR, USERNAME_DUPLICATION_ERROR_MESSAGE);
            return "user/edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        LOGGER.info("/users/delete/{} POST" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        userService.delete(id);
        return REDIRECT_TO_USERS_MENU;
    }



}
