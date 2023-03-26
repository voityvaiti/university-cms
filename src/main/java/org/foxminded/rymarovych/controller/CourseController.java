package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.model.Course;
import org.foxminded.rymarovych.service.abstractions.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseController.class);

    static final String REQUEST_RECEIVING_LOG_MESSAGE = " HTTP request received";

    static final String REDIRECT_TO_COURSES_MENU = "redirect:/courses/";

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @GetMapping("/")
    public String index() {
        LOGGER.debug("/courses/ GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        return "course/menu";
    }


    @GetMapping("/all")
    public String all(Model model) {
        LOGGER.debug("/courses/all GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        model.addAttribute("courses", courseService.getAllCoursesList());
        return "course/list";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        LOGGER.debug("/courses/show/{} GET" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        model.addAttribute("optionalCourse", courseService.findById(id));

        return "course/show";
    }

    @GetMapping("/new")
    public String newCourse(Model model) {
        LOGGER.debug("/courses/new GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        model.addAttribute("course", new Course());
        return "course/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("course") Course course) {
        LOGGER.debug("/courses/new POST" + REQUEST_RECEIVING_LOG_MESSAGE);

        courseService.add(course);
        LOGGER.debug("Course added: {}", course);

        return REDIRECT_TO_COURSES_MENU;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        LOGGER.debug("/courses/edit/{} GET" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        Optional<Course> optionalCourse = courseService.findById(id);

        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();

            LOGGER.debug("Found Course to edit: {}", course);

            model.addAttribute("course", course);

        } else {
            LOGGER.warn("Not found Course to edit by ID: {}", id);

            model.addAttribute("course", new Course());
            model.addAttribute("errorMessage", "No such Course");

        }
        return "course/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@ModelAttribute("course") Course course, @PathVariable("id") Long id) {
        LOGGER.debug("/courses/edit/{} POST" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        courseService.update(id, course);
        return REDIRECT_TO_COURSES_MENU;

    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        LOGGER.info("/courses/delete/{}" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        courseService.delete(id);
        return REDIRECT_TO_COURSES_MENU;
    }

    @GetMapping("/group-relation/add/{id}")
    public String addGroupRelation(@PathVariable("id") Long id, Model model) {

        model.addAttribute("courseId", id);
        model.addAttribute("groups", courseService.getUnlinkedGroups(id));

        return "/course/add-group";
    }

    @PostMapping("/group-relation/{action}")
    public String editGroupRelation(@PathVariable("action") String action, @RequestParam("courseId") Long courseId, @RequestParam("groupId") Long groupId) {

        if(action.equals("link")) {
            courseService.linkGroup(courseId, groupId);

        } else if (action.equals("unlink")) {
            courseService.unlinkGroup(courseId, groupId);
        }
        return "redirect:/courses/show/" + courseId;
    }

    @GetMapping("/teacher-relation/add/{id}")
    public String addTeacherRelation(@PathVariable("id") Long id, Model model) {

        model.addAttribute("courseId", id);
        model.addAttribute("teachers", courseService.getUnlinkedTeachers(id));

        return "/course/add-teacher";
    }

    @PostMapping("/teacher-relation/{action}")
    public String editTeacherRelation(@PathVariable("action") String action, @RequestParam("courseId") Long courseId, @RequestParam("teacherId") Long teacherId) {

        if (action.equals("link")) {
            courseService.linkTeacher(courseId, teacherId);

        } else if (action.equals("unlink")) {
            courseService.unlinkTeacher(courseId, teacherId);
        }

        return "redirect:/courses/show/" + courseId;
    }

}
