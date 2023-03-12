package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.service.abstractions.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/get-list")
    public String getList(Model model) {
        model.addAttribute("teachers", teacherService.getAllTeachersList());
        return "teachers-list";
    }
}
