package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.service.abstractions.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseController.class);

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/all")
    public String getList(Model model) {
        LOGGER.debug("/courses/all GET HTTP request received");

        model.addAttribute("courses", courseService.getAllCoursesList());
        return "courses-list";
    }

}
