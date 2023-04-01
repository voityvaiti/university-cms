package org.foxminded.rymarovych.controller;


import org.foxminded.rymarovych.model.Student;
import org.foxminded.rymarovych.service.abstractions.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);

    static final String REQUEST_RECEIVING_LOG_MESSAGE = " HTTP request received";

    static final String REDIRECT_TO_STUDENTS_MENU = "redirect:/students";

    static final String REDIRECT_TO_STUDENT_SHOW = "redirect:/students/show/";

    static final String ERROR_MESSAGE_ATTR = "errorMessage";

    static final String STUDENT_ATTR = "student";

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping("")
    public String index() {
        LOGGER.debug("/students GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        return "student/menu";
    }


    @GetMapping("/all")
    public String getList(Model model) {
        LOGGER.debug("/students/all GET HTTP request received");

        model.addAttribute("students", studentService.getAllStudentsList());
        return "student/list";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        LOGGER.debug("/students/show/{} GET" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        model.addAttribute("optionalStudent", studentService.findById(id));

        return "student/show";
    }

    @GetMapping("/new")
    public String newStudent(Model model) {
        LOGGER.debug("/students/new GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        model.addAttribute(STUDENT_ATTR, new Student());
        model.addAttribute("groups", studentService.getAllGroups());

        return "student/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute(STUDENT_ATTR) Student student, @RequestParam(name = "groupId") Optional<Long> optionalGroupId) {
        LOGGER.debug("/students/new POST" + REQUEST_RECEIVING_LOG_MESSAGE);

        studentService.add(student);
        LOGGER.debug("Student added: {}", student);

        if (optionalGroupId.isPresent()) {
            Long groupId = optionalGroupId.get();

            LOGGER.debug("Noticed groupId: {} to link with new Student: {}. Linking.", groupId, student);

            studentService.linkGroup(student.getId(), groupId);
        }

        return REDIRECT_TO_STUDENTS_MENU;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        LOGGER.debug("/students/edit/{} GET" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        Optional<Student> optionalStudent = studentService.findById(id);

        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();

            LOGGER.debug("Found Student to edit: {}", student);

            model.addAttribute(STUDENT_ATTR, student);
            model.addAttribute("groups", studentService.getAllGroups());

        } else {
            LOGGER.warn("Not found Student to edit by ID: {}", id);

            model.addAttribute(STUDENT_ATTR, new Student());
            model.addAttribute(ERROR_MESSAGE_ATTR, "No such Student");

        }
        return "student/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@ModelAttribute(STUDENT_ATTR) Student student, @PathVariable("id") Long studentId, @RequestParam(name = "groupId") Optional<Long> optionalGroupId) {
        LOGGER.debug("/students/edit/{} POST" + REQUEST_RECEIVING_LOG_MESSAGE, studentId);

        studentService.update(studentId, student);

        if (optionalGroupId.isPresent()) {
            Long groupId = optionalGroupId.get();

            LOGGER.debug("Noticed Group ID: {} to link with updated Student ID: {}. Linking...", groupId, studentId);

            studentService.linkGroup(studentId, groupId);
        }

        return REDIRECT_TO_STUDENT_SHOW + studentId;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        LOGGER.info("/students/delete/{} POST" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        studentService.delete(id);
        return REDIRECT_TO_STUDENTS_MENU;
    }

    @PostMapping("/group-relation/unlink/{student-id}")
    public String unlinkGroup(@PathVariable("student-id") Long id) {
        LOGGER.debug("/students/group-relation/unlink/{} POST" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        studentService.unlinkGroup(id);

        return REDIRECT_TO_STUDENT_SHOW + id;
    }

}
