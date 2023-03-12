package org.foxminded.rymarovych.controller;


import org.foxminded.rymarovych.service.abstractions.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/get-list")
    public String getList(Model model) {
        model.addAttribute("students", studentService.getAllStudentsList());
        return "students-list";
    }
}
