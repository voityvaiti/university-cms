package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.GroupRepository;
import org.foxminded.rymarovych.model.Group;
import org.foxminded.rymarovych.service.abstractions.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<Group> findById(Long id) {
        LOGGER.debug("Returning Optional Group, found by ID: {}", id);

        return groupRepository.findById(id);
    }

    @Override
    public Group add(Group group) {
        LOGGER.debug("Saving new Group: {}", group);

        return groupRepository.save(group);
    }

    @Override
    public Group update(Long id, Group group) {
        LOGGER.debug("Received Group to update: {} by ID: {}", group, id);

        Optional<Group> optionalCurrentGroup = groupRepository.findById(id);

        if(optionalCurrentGroup.isPresent()) {
            Group currentGroup = optionalCurrentGroup.get();

            LOGGER.debug("Found Group to update: {} by ID: {}", currentGroup, id);

            currentGroup.setName(group.getName());
            currentGroup.setSpeciality(group.getSpeciality());
            currentGroup.setYear(group.getYear());

            LOGGER.debug("Saving updated Group: {}", currentGroup);

            return groupRepository.save(currentGroup);

        } else {
            LOGGER.warn("Not found Group to update by ID: {}", id);

            return new Group();
        }
    }

    @Override
    public void delete(Long id) {
        LOGGER.info("Deleting Group by ID: {}", id);

        groupRepository.deleteById(id);
    }

}
