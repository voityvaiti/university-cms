package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.service.abstractions.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherController.class);


    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/all")
    public String getList(Model model) {
        LOGGER.debug("/teachers/all GET HTTP request received");

        model.addAttribute("teachers", teacherService.getAllTeachersList());
        return "teachers-list";
    }
}
