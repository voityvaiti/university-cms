package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.model.User;
import org.foxminded.rymarovych.service.abstractions.UserService;
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

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "user/menu";
    }

    @GetMapping("/all")
    public String getList(Model model) {
        model.addAttribute("users", userService.getAllUsersList());
        return "user/list";
    }

    @GetMapping("/show/{id}")
    public String showUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("optionalUser", userService.findById(id));
        return "user/show";
    }

    @GetMapping("/current")
    public String getCurrent(Model model) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "user/show-info";
    }
    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "user/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("user") User user, Model model) {

        if(userService.findByUsername(user.getUsername()).isPresent()) {

            model.addAttribute("user", user);
            model.addAttribute("errorMessage", USERNAME_DUPLICATION_ERROR_MESSAGE);
            return "user/new";
        }
        userService.add(user);
        return "redirect:/users/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {

        Optional<User> optionalUser = userService.findById(id);

        if(optionalUser.isPresent()) {
            model.addAttribute("user", optionalUser.get());

        } else {
            model.addAttribute("user", new User());
            model.addAttribute("errorMessage", "No such user");

        }
        return "user/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@ModelAttribute("user") User user, Model model, @PathVariable("id") Long id) {

        Optional<User> potentialUserWithSameUsername = userService.findByUsername(user.getUsername());

        if(potentialUserWithSameUsername.isEmpty() || potentialUserWithSameUsername.get().getId().equals(id)) {
            userService.update(id, user);
            return "redirect:/users/";

        } else {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", USERNAME_DUPLICATION_ERROR_MESSAGE);
            return "user/edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/users/";
    }



}
