package org.foxminded.rymarovych.controller;

import org.foxminded.rymarovych.service.abstractions.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/all")
    public String getList(Model model) {
        model.addAttribute("groups", groupService.getAllGroupsList());
        return "groups-list";
    }

}
