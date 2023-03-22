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

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        LOGGER.debug("/users/ GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        return "user/menu";
    }

    @GetMapping("/all")
    public String getList(Model model) {
        LOGGER.debug("/users/all GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        model.addAttribute("users", userService.getAllUsersList());
        return "user/list";
    }

    @GetMapping("/show/{id}")
    public String showUser(@PathVariable("id") Long id, Model model) {
        LOGGER.debug("/users/show/{} GET" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        model.addAttribute("optionalUser", userService.findById(id));
        return "user/show";
    }

    @GetMapping("/current")
    public String getCurrent(Model model) {
        LOGGER.debug("/users/current GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "user/show-info";
    }
    @GetMapping("/new")
    public String newUser(Model model) {
        LOGGER.debug("/users/new GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        model.addAttribute("user", new User());
        return "user/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("user") User user, Model model) {
        LOGGER.debug("/users/ POST" + REQUEST_RECEIVING_LOG_MESSAGE);

        if(userService.findByUsername(user.getUsername()).isPresent()) {
            LOGGER.debug("Received User to create with duplicated username: {} ", user.getUsername());

            model.addAttribute("user", user);
            model.addAttribute("errorMessage", USERNAME_DUPLICATION_ERROR_MESSAGE);
            return "user/new";
        }
        userService.add(user);
        LOGGER.debug("User added. Username: {}", user.getUsername());

        return "redirect:/users/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        LOGGER.debug("/users/edit/{} GET" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        Optional<User> optionalUser = userService.findById(id);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();

            LOGGER.debug("Found user to edit. ID: {}, username: {}", user.getId(), user.getUsername());

            model.addAttribute("user", optionalUser.get());

        } else {
            LOGGER.warn("Not found user to edit by ID: {}", id);

            model.addAttribute("user", new User());
            model.addAttribute("errorMessage", "No such user");

        }
        return "user/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@ModelAttribute("user") User user, Model model, @PathVariable("id") Long id) {
        LOGGER.debug("/users/edit/{} POST" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        Optional<User> potentialUserWithSameUsername = userService.findByUsername(user.getUsername());

        if(potentialUserWithSameUsername.isEmpty() || potentialUserWithSameUsername.get().getId().equals(id)) {
            LOGGER.debug("Username duplication of received User is not detected. Updating User: {}", user);

            userService.update(id, user);
            return "redirect:/users/";

        } else {
            LOGGER.debug("Detected username duplication of received User: {}. Update was not executed", user);

            model.addAttribute("user", user);
            model.addAttribute("errorMessage", USERNAME_DUPLICATION_ERROR_MESSAGE);
            return "user/edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        LOGGER.info("/users/delete/{}" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        userService.delete(id);
        return "redirect:/users/";
    }



}
