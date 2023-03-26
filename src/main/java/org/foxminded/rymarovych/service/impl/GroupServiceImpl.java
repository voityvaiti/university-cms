package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.CourseRepository;
import org.foxminded.rymarovych.dao.GroupRepository;
import org.foxminded.rymarovych.model.Course;
import org.foxminded.rymarovych.model.Group;
import org.foxminded.rymarovych.service.abstractions.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, CourseRepository courseRepository) {
        this.groupRepository = groupRepository;
        this.courseRepository = courseRepository;
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
    public List<Course> getUnlinkedCourses(Long groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);

        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();
            List<Course> allCourses = courseRepository.findAll();

            allCourses.removeAll(group.getCourses());

            return allCourses;

        } else {
            return new ArrayList<>();
        }
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

        if (optionalCurrentGroup.isPresent()) {
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

    @Override
    @Transactional
    public void linkCourse(Long groupId, Long courseId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if(optionalGroup.isPresent() && optionalCourse.isPresent()) {
            optionalGroup.get().addCourse(optionalCourse.get());
        }
    }

    @Override
    @Transactional
    public void unlinkCourse(Long groupId, Long courseId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if(optionalGroup.isPresent() && optionalCourse.isPresent()) {
            optionalGroup.get().removeCourse(optionalCourse.get());
        }
    }


}
