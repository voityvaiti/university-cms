package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.model.Teacher;
import org.foxminded.rymarovych.service.abstractions.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherController.class);

    static final String REQUEST_RECEIVING_LOG_MESSAGE = " HTTP request received";

    static final String REDIRECT_TO_TEACHERS_MENU = "redirect:/teachers";

    static final String REDIRECT_TO_TEACHER_SHOW = "redirect:/teachers/show/";

    static final String ERROR_MESSAGE_ATTR = "errorMessage";

    static final String TEACHER_ATTR = "teacher";


    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }


    @GetMapping()
    public String index() {
        LOGGER.debug("/teachers GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        return "teacher/menu";
    }

    @GetMapping("/all")
    public String getList(Model model) {
        LOGGER.debug("/teachers/all GET HTTP request received");

        model.addAttribute("teachers", teacherService.getAllTeachersList());
        return "teacher/list";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        LOGGER.debug("/teachers/show/{} GET" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        model.addAttribute("optionalTeacher", teacherService.findById(id));

        return "teacher/show";
    }

    @GetMapping("/new")
    public String newTeacher(Model model) {
        LOGGER.debug("/teachers/new GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        model.addAttribute(TEACHER_ATTR, new Teacher());
        return "teacher/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute(TEACHER_ATTR) Teacher teacher) {
        LOGGER.debug("/teachers/new POST" + REQUEST_RECEIVING_LOG_MESSAGE);

        teacherService.add(teacher);
        LOGGER.debug("Teacher added: {}", teacher);

        return REDIRECT_TO_TEACHERS_MENU;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        LOGGER.debug("/teachers/edit/{} GET" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        Optional<Teacher> optionalTeacher = teacherService.findById(id);

        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();

            LOGGER.debug("Found Teacher to edit: {}", teacher);

            model.addAttribute(TEACHER_ATTR, teacher);

        } else {
            LOGGER.warn("Not found Teacher to edit by ID: {}", id);

            model.addAttribute(TEACHER_ATTR, new Teacher());
            model.addAttribute(ERROR_MESSAGE_ATTR, "No such Teacher");

        }
        return "teacher/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@ModelAttribute(TEACHER_ATTR) Teacher teacher, @PathVariable("id") Long id) {
        LOGGER.debug("/teachers/edit/{} POST" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        teacherService.update(id, teacher);
        return REDIRECT_TO_TEACHER_SHOW + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        LOGGER.debug("/teachers/delete/{} POST" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        teacherService.delete(id);
        return REDIRECT_TO_TEACHERS_MENU;
    }

    @GetMapping("/course-relation/add/{teacher-id}")
    public String addCourseRelation(@PathVariable("teacher-id") Long teacherId, Model model) {
        LOGGER.debug("/teachers/course-relation/add/{} GET" + REQUEST_RECEIVING_LOG_MESSAGE, teacherId);

        model.addAttribute("teacherId", teacherId);
        model.addAttribute("courses", teacherService.getUnlinkedCourses(teacherId));

        return "/teacher/add-course";
    }

    @PostMapping("/course-relation/{action}")
    public String editCourseRelation(@PathVariable("action") String action, @RequestParam("teacherId") Long teacherId, @RequestParam("courseId") Long courseId) {
        LOGGER.debug("/teachers/course-relation/{} POST" + REQUEST_RECEIVING_LOG_MESSAGE, action);

        if (action.equals("link")) {
            LOGGER.debug("Linking Teacher ID: {} to the Course ID: {}", teacherId, courseId);

            teacherService.linkCourse(teacherId, courseId);

        } else if (action.equals("unlink")) {
            LOGGER.debug("Unlinking Teacher ID: {} from the Course ID: {}", teacherId, courseId);

            teacherService.unlinkCourse(teacherId, courseId);
        } else {
            LOGGER.warn("Action {} not recognized. Teacher ID: {}, Course ID: {}", action, teacherId, courseId);
        }

        return REDIRECT_TO_TEACHER_SHOW + teacherId;
    }

}
