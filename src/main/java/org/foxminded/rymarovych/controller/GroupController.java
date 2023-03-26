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

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/")
    public String index() {
        LOGGER.debug("/groups/ GET" + REQUEST_RECEIVING_LOG_MESSAGE);

        return "group/menu";
    }

    @GetMapping("/all")
    public String all(Model model) {
        LOGGER.debug("/groups/all GER HTTP request received");

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

        model.addAttribute("group", new Group());
        return "group/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("group") Group group) {
        LOGGER.debug("/groups/new POST" + REQUEST_RECEIVING_LOG_MESSAGE);

        groupService.add(group);
        LOGGER.debug("Group added: {}", group);

        return "redirect:/groups/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        LOGGER.debug("/groups/edit/{} GET" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        Optional<Group> optionalGroup = groupService.findById(id);

        if(optionalGroup.isPresent()) {
            Group group = optionalGroup.get();

            LOGGER.debug("Found Group to edit: {}", group);

            model.addAttribute("group", group);

        } else {
            LOGGER.warn("Not found Group to edit by ID: {}", id);

            model.addAttribute("group", new Group());
            model.addAttribute("errorMessage", "No such Group");

        }
        return "group/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@ModelAttribute("group") Group group, @PathVariable("id") Long id) {
        LOGGER.debug("/groups/edit/{} POST" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        groupService.update(id, group);
        return "redirect:/groups/";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        LOGGER.info("/groups/delete/{}" + REQUEST_RECEIVING_LOG_MESSAGE, id);

        groupService.delete(id);
        return "redirect:/groups/";
    }

    @GetMapping("/course-relation/add/{id}")
    public String addCourseRelation(@PathVariable("id") Long id, Model model) {

        model.addAttribute("groupId", id);
        model.addAttribute("courses", groupService.getUnlinkedCourses(id));

        return "/group/add-course";
    }

    @PostMapping("/course-relation/{action}")
    public String editGroupRelation(@PathVariable("action") String action, @RequestParam("groupId") Long groupId, @RequestParam("courseId") Long courseId) {

        if(action.equals("link")) {
            groupService.linkCourse(groupId, courseId);

        } else if (action.equals("unlink")) {
            groupService.unlinkCourse(groupId, courseId);
        }
        return "redirect:/groups/show/" + groupId;
    }


}
