package org.foxminded.rymarovych.service;

import org.foxminded.rymarovych.dao.GroupRepository;
import org.foxminded.rymarovych.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void addGroup(Group group) {
        groupRepository.save(group);
    }
}
