package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.CourseRepository;
import org.foxminded.rymarovych.dao.GroupRepository;
import org.foxminded.rymarovych.dao.StudentRepository;
import org.foxminded.rymarovych.model.Course;
import org.foxminded.rymarovych.model.Group;
import org.foxminded.rymarovych.model.Student;
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
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
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
    public List<Student> getUnlinkedStudents() {
        LOGGER.debug("Returning list of Students with NULL Groups.");

        return studentRepository.findStudentByGroupNull();
    }

    @Override
    public List<Course> getUnlinkedCourses(Long groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);

        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();

            LOGGER.debug("Found Group to get unlinked Courses: {}", group);

            List<Course> allCourses = courseRepository.findAll();

            allCourses.removeAll(group.getCourses());

            LOGGER.debug("Got list of unlinked Courses: {} to the Group: {}", allCourses, group);

            return allCourses;

        } else {
            LOGGER.warn("Not found any Group by ID: {}. Returning an empty ArrayList.", groupId);

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
    public void linkStudent(Long groupId, Long studentId) {
        LOGGER.debug("Received groupId: {} to link with studentId: {}", groupId, studentId);

        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        Optional<Student> optionalStudent = studentRepository.findById(studentId);

        if(optionalGroup.isPresent() && optionalStudent.isPresent()) {
            Group group = optionalGroup.get();
            Student student = optionalStudent.get();

            LOGGER.debug("Found Group: {} to link with Student: {}. Linking.", group, student);

            student.setGroup(group);

        } else {
            LOGGER.warn("Not found Group and/or Student to link. Group: {}, Student: {}", optionalGroup.isPresent(), optionalStudent.isPresent());
        }
    }

    @Override
    @Transactional
    public void unlinkStudent(Long studentId) {
        LOGGER.debug("Received studentId: {} to unlink Group.", studentId);

        Optional<Student> optionalStudent = studentRepository.findById(studentId);

        if(optionalStudent.isPresent()) {
            Student student = optionalStudent.get();

            LOGGER.debug("Found Group: {} to unlink from Group: {}. Unlinking.", student.getGroup(), student);

            student.setGroup(null);
        } else {
            LOGGER.warn("Not found Student by ID: {} to unlink Group.", studentId);
        }

    }

    @Override
    @Transactional
    public void linkCourse(Long groupId, Long courseId) {
        LOGGER.debug("Received groupId: {} to link with courseId: {}", groupId, courseId);

        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalGroup.isPresent() && optionalCourse.isPresent()) {
            Group group = optionalGroup.get();
            Course course = optionalCourse.get();

            LOGGER.debug("Found Group: {} to link with Course: {}. Linking.", group, course);

            group.addCourse(course);
        } else {
            LOGGER.warn("Not found Group and/or Course to link. Group: {}, Course: {}", optionalGroup.isPresent(), optionalCourse.isPresent());
        }
    }

    @Override
    @Transactional
    public void unlinkCourse(Long groupId, Long courseId) {
        LOGGER.debug("Received groupId: {} to unlink from courseId: {}", groupId, courseId);

        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalGroup.isPresent() && optionalCourse.isPresent()) {
            Group group = optionalGroup.get();
            Course course = optionalCourse.get();

            LOGGER.debug("Found Group: {} to unlink with Course: {}. Unlinking.", group, course);

            group.removeCourse(course);
        } else {
            LOGGER.warn("Not found Group and/or Course to unlink. Group: {}, Course: {}", optionalGroup.isPresent(), optionalCourse.isPresent());
        }
    }


}
