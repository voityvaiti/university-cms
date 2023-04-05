package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.model.Group;
import org.foxminded.rymarovych.service.abstractions.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/groups")
public class GroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);

    static final String REQUEST_RECEIVING_LOG_MESSAGE = " HTTP request received";

    static final String REDIRECT_TO_GROUPS_MENU = "redirect:/groups";

    static final String REDIRECT_TO_GROUP_SHOW = "redirect:/groups/show/";

    static final String ERROR_MESSAGE_ATTR = "errorMessage";

    static final String GROUP_ATTR = "group";

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping()
    public String index() {
        LOGGER.debug("/groups GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        return "group/menu";
    }

    @GetMapping("/all")
    public String all(Model model) {
        LOGGER.debug("/groups/all GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        model.addAttribute("groups", groupService.getAllGroupsList());
        return "group/list";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        LOGGER.debug("/groups/show/{} GET" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        model.addAttribute("optionalGroup", groupService.findById(id));
        return "group/show";
    }

    @GetMapping("/new")
    public String newGroup(Model model) {
        LOGGER.debug("/groups/new GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        model.addAttribute(GROUP_ATTR, new Group());
        return "group/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute(GROUP_ATTR) Group group) {
        LOGGER.debug("/groups/new POST" + REQUEST_RECEIVING_LOG_MESSAGE);

        groupService.add(group);
        LOGGER.debug("Group added: {}", group);

        return REDIRECT_TO_GROUPS_MENU;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        LOGGER.debug("/groups/edit/{} GET" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        Optional<Group> optionalGroup = groupService.findById(id);

        if(optionalGroup.isPresent()) {
            Group group = optionalGroup.get();

            LOGGER.debug("Found Group to edit: {}", group);

            model.addAttribute(GROUP_ATTR, group);

        } else {
            LOGGER.warn("Not found Group to edit by ID: {}", id);

            model.addAttribute(GROUP_ATTR, new Group());
            model.addAttribute(ERROR_MESSAGE_ATTR, "No such Group");

        }
        return "group/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@ModelAttribute(GROUP_ATTR) Group group, @PathVariable("id") Long id) {
        LOGGER.debug("/groups/edit/{} POST" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        groupService.update(id, group);
        return REDIRECT_TO_GROUP_SHOW + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        LOGGER.info("/groups/delete/{} POST" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        groupService.delete(id);
        return REDIRECT_TO_GROUPS_MENU;
    }

    @GetMapping("/student-relation/add/{group-id}")
    public String addStudentRelation(@PathVariable("group-id") Long groupId, Model model) {
        LOGGER.debug("/groups/student-relation/add/{} GET" + REQUEST_RECEIVING_LOG_MESSAGE, groupId);

        model.addAttribute("groupId", groupId);
        model.addAttribute("students", groupService.getUnlinkedStudents());

        return "/group/add-student";
    }

    @PostMapping("/student-relation/{action}")
    public String editStudentRelation(@PathVariable("action") String action, @RequestParam("groupId") Long groupId, @RequestParam("studentId") Long studentId) {
        LOGGER.debug("/groups/student-relation/{} POST" + REQUEST_RECEIVING_LOG_MESSAGE, action);

        if(action.equals("link")) {
            LOGGER.debug("Linking Group ID: {} to the Student ID: {}", groupId, studentId);

            groupService.linkStudent(groupId, studentId);

        } else if (action.equals("unlink")) {
            LOGGER.debug("Unlinking Group ID: {} from the Student ID: {}", groupId, studentId);

            groupService.unlinkStudent(studentId);
        } else {
            LOGGER.warn("Action {} not recognized. Group ID: {}, Student ID: {}", action, groupId, studentId);
        }

        return REDIRECT_TO_GROUP_SHOW + groupId;
    }

    @GetMapping("/course-relation/add/{group-id}")
    public String addCourseRelation(@PathVariable("group-id") Long groupId, Model model) {
        LOGGER.debug("/groups/course-relation/add/{} GET" + REQUEST_RECEIVING_LOG_MESSAGE, groupId);

        model.addAttribute("groupId", groupId);
        model.addAttribute("courses", groupService.getUnlinkedCourses(groupId));

        return "/group/add-course";
    }

    @PostMapping("/course-relation/{action}")
    public String editGroupRelation(@PathVariable("action") String action, @RequestParam("groupId") Long groupId, @RequestParam("courseId") Long courseId) {
        LOGGER.debug("/groups/course-relation/{} POST" + REQUEST_RECEIVING_LOG_MESSAGE, action);

        if(action.equals("link")) {
            LOGGER.debug("Linking Group ID: {} to the Course ID: {}", groupId, courseId);

            groupService.linkCourse(groupId, courseId);

        } else if (action.equals("unlink")) {
            LOGGER.debug("Unlinking Group ID: {} from the Course ID: {}", groupId, courseId);

            groupService.unlinkCourse(groupId, courseId);
        } else {
            LOGGER.warn("Action {} not recognized. Group ID: {}, Course ID: {}", action, groupId, courseId);
        }

        return REDIRECT_TO_GROUP_SHOW + groupId;
    }


}
