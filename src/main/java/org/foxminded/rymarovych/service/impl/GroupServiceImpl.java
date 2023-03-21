package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.GroupRepository;
import org.foxminded.rymarovych.model.Group;
import org.foxminded.rymarovych.service.abstractions.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<Group> getAllGroupsList() {
        LOGGER.debug("Returning all groups list");

        return groupRepository.findAll();
    }

}
