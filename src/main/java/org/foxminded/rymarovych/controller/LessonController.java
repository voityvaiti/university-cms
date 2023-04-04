package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.model.Lesson;
import org.foxminded.rymarovych.service.abstractions.LessonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/lessons")
public class LessonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LessonController.class);

    static final String REQUEST_RECEIVING_LOG_MESSAGE = " HTTP request received";

    static final String REDIRECT_TO_LESSONS_MENU = "redirect:/lessons";

    static final String REDIRECT_TO_LESSON_SHOW = "redirect:/lessons/show/";

    static final String ERROR_MESSAGE_ATTR = "errorMessage";

    static final String LESSON_ATTR = "lesson";


    private final LessonService lessonService;

    @Autowired
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }


    @GetMapping("")
    public String index() {
        LOGGER.debug("/lessons GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        return "lesson/menu";
    }

    @GetMapping("/all")
    public String all(Model model) {
        LOGGER.debug("/lessons/all GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        model.addAttribute("lessonsForDays", lessonService.getAllLessonsForDaysSortedList());
        return "lesson/list";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        LOGGER.debug("/lessons/show/{} GET" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        model.addAttribute("optionalLesson", lessonService.findById(id));

        return "lesson/show";
    }

    @GetMapping("/new")
    public String newLesson(Model model) {
        LOGGER.debug("/lessons/new GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        model.addAttribute(LESSON_ATTR, new Lesson());

        model.addAttribute("courses", lessonService.getAllCourses());
        model.addAttribute("teachers", lessonService.getAllTeachers());
        model.addAttribute("groups", lessonService.getAllGroups());

        return "lesson/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute(LESSON_ATTR) Lesson lesson, @RequestParam("groupId") Optional<Long> optionalGroupId) {
        LOGGER.debug("/lessons/new POST" + REQUEST_RECEIVING_LOG_MESSAGE);

        Lesson addedLesson = lessonService.add(lesson);

        LOGGER.debug("Lesson added: {}", addedLesson);

        if (optionalGroupId.isPresent()) {
            Long groupId = optionalGroupId.get();

            lessonService.linkGroup(lesson.getId(), groupId);

            LOGGER.debug("Linked Group ID: {} to recently added Lesson ID: {}", groupId, lesson.getId());
        }

        return REDIRECT_TO_LESSONS_MENU;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        LOGGER.debug("/lessons/edit/{} GET" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        Optional<Lesson> optionalLesson = lessonService.findById(id);

        if (optionalLesson.isPresent()) {
            Lesson lesson = optionalLesson.get();

            LOGGER.debug("Found Lesson to edit: {}", lesson);

            model.addAttribute(LESSON_ATTR, lesson);

            model.addAttribute("courses", lessonService.getAllCourses());
            model.addAttribute("teachers", lessonService.getAllTeachers());

        } else {
            LOGGER.warn("Not found Lesson to edit by ID: {}", id);

            model.addAttribute(LESSON_ATTR, new Lesson());
            model.addAttribute(ERROR_MESSAGE_ATTR, "No such Lesson");

        }
        return "lesson/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@ModelAttribute(LESSON_ATTR) Lesson lesson, @PathVariable("id") Long id) {
        LOGGER.debug("/lessons/edit/{} POST" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        lessonService.update(id, lesson);
        return REDIRECT_TO_LESSON_SHOW + id;
    }


    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        LOGGER.debug("/lessons/delete/{} POST" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        lessonService.delete(id);
        return REDIRECT_TO_LESSONS_MENU;
    }

    @GetMapping("/group-relation/add/{lesson-id}")
    public String addGroupRelation(@PathVariable("lesson-id") Long lessonId, Model model) {
        LOGGER.debug("/lessons/group-relation/add/{} GET" + REQUEST_RECEIVING_LOG_MESSAGE, lessonId);

        model.addAttribute("lessonId", lessonId);
        model.addAttribute("groups", lessonService.getUnlinkedGroups(lessonId));

        return "/lesson/add-group";
    }

    @PostMapping("/group-relation/{action}")
    public String editGroupRelation(@PathVariable("action") String action, @RequestParam("lessonId") Long lessonId, @RequestParam("groupId") Long groupId) {
        LOGGER.debug("/lessons/group-relation/{} POST" + REQUEST_RECEIVING_LOG_MESSAGE, action);

        if (action.equals("link")) {
            LOGGER.debug("Linking Lesson ID: {} to the Group ID: {}", lessonId, groupId);

            lessonService.linkGroup(lessonId, groupId);

        } else if (action.equals("unlink")) {
            LOGGER.debug("Unlinking Lesson ID: {} from the Group ID: {}", lessonId, groupId);

            lessonService.unlinkGroup(lessonId, groupId);
        } else {
            LOGGER.warn("Action {} not recognized. Lesson ID: {}, Group ID: {}", action, lessonId, groupId);
        }

        return REDIRECT_TO_LESSON_SHOW + lessonId;
    }
}
