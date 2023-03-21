package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.service.abstractions.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/groups")
public class GroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/all")
    public String getList(Model model) {
        LOGGER.debug("/groups/all GER HTTP request received");

        model.addAttribute("groups", groupService.getAllGroupsList());
        return "groups-list";
    }

}
