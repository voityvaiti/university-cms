package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.CourseRepository;
import org.foxminded.rymarovych.dao.GroupRepository;
import org.foxminded.rymarovych.dao.TeacherRepository;
import org.foxminded.rymarovych.model.Course;
import org.foxminded.rymarovych.model.Group;
import org.foxminded.rymarovych.model.Teacher;
import org.foxminded.rymarovych.service.abstractions.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseRepository courseRepository;
    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, GroupRepository groupRepository, TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.groupRepository = groupRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public List<Course> getAllCoursesList() {
        LOGGER.debug("Returning all courses list");

        return courseRepository.findAll();
    }

    @Override
    public Optional<Course> findById(Long id) {
        LOGGER.debug("Returning Optional Course, found by ID: {}", id);

        return courseRepository.findById(id);
    }

    @Override
    public List<Group> getUnlinkedGroups(Long courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();

            LOGGER.debug("Found Course to get unlinked Groups: {}", course);

            List<Group> allGroups = groupRepository.findAll();

            allGroups.removeAll(course.getGroups());

            LOGGER.debug("Got list of unlinked Groups: {} to the Course: {}", allGroups, course);

            return allGroups;

        } else {
            LOGGER.warn("Not found any Course by ID: {}. Returning an empty ArrayList.", courseId);

            return new ArrayList<>();
        }
    }

    @Override
    public List<Teacher> getUnlinkedTeachers(Long courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();

            LOGGER.debug("Found Course to get unlinked Teachers: {}", course);

            List<Teacher> allTeachers = teacherRepository.findAll();

            allTeachers.removeAll(course.getTeachers());

            LOGGER.debug("Got list of unlinked Teachers: {} to the Course: {}", allTeachers, course);

            return allTeachers;

        } else {
            LOGGER.warn("Not found any Course by ID: {}. Returning an empty ArrayList.", courseId);

            return new ArrayList<>();
        }
    }


    @Override
    public Course add(Course course) {

        LOGGER.debug("Saving new Course: {}", course);

        return courseRepository.save(course);
    }

    @Override
    public Course update(Long id, Course course) {
        LOGGER.debug("Received Course to update: {} by ID: {}", course, id);

        Optional<Course> optionalCurrentCourse = courseRepository.findById(id);

        if (optionalCurrentCourse.isPresent()) {
            Course currentCourse = optionalCurrentCourse.get();

            LOGGER.debug("Found Course to update: {} by ID: {}", currentCourse, id);

            currentCourse.setName(course.getName());

            LOGGER.debug("Saving updated Course: {}", currentCourse);

            return courseRepository.save(currentCourse);

        } else {
            LOGGER.warn("Not found Course to update by ID: {}", id);

            return new Course();
        }
    }

    @Override
    public void delete(Long id) {
        LOGGER.info("Deleting Course by ID: {}", id);

        courseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void linkGroup(Long courseId, Long groupId) {
        LOGGER.debug("Received courseId: {} to link with groupId: {}", courseId, groupId);

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        Optional<Group> optionalGroup = groupRepository.findById(groupId);

        if (optionalCourse.isPresent() && optionalGroup.isPresent()) {
            Course course = optionalCourse.get();
            Group group = optionalGroup.get();

            LOGGER.debug("Found Course: {} to link with Group: {}. Linking.", course, group);

            course.addGroup(group);
        } else {
            LOGGER.warn("Not found Course and/or Group to link. Course: {}, Group: {}", optionalCourse.isPresent(), optionalGroup.isPresent());
        }
    }

    @Override
    @Transactional
    public void unlinkGroup(Long courseId, Long groupId) {
        LOGGER.debug("Received courseId: {} to unlink from groupId: {}", courseId, groupId);

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        Optional<Group> optionalGroup = groupRepository.findById(groupId);

        if (optionalCourse.isPresent() && optionalGroup.isPresent()) {
            Course course = optionalCourse.get();
            Group group = optionalGroup.get();

            LOGGER.debug("Found Course: {} to unlink from Group: {}. Unlinking.", course, group);

            course.removeGroup(group);
        } else {
            LOGGER.warn("Not found Course and/or Group to unlink. Course: {}, Group: {}", optionalCourse.isPresent(), optionalGroup.isPresent());
        }
    }

    @Override
    @Transactional
    public void linkTeacher(Long courseId, Long teacherId) {
        LOGGER.debug("Received courseId: {} to link with teacherId: {}", courseId, teacherId);

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);

        if (optionalCourse.isPresent() && optionalTeacher.isPresent()) {
            Course course = optionalCourse.get();
            Teacher teacher = optionalTeacher.get();

            LOGGER.debug("Found Course: {} to link with Teacher: {}. Linking.", course, teacher);

            course.addTeacher(teacher);
        } else {
            LOGGER.warn("Not found Course and/or Teacher to link. Course: {}, Teacher: {}", optionalCourse.isPresent(), optionalTeacher.isPresent());
        }
    }

    @Override
    @Transactional
    public void unlinkTeacher(Long courseId, Long teacherId) {
        LOGGER.debug("Received courseId: {} to unlink from teacherId: {}", courseId, teacherId);

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);

        if (optionalCourse.isPresent() && optionalTeacher.isPresent()) {
            Course course = optionalCourse.get();
            Teacher teacher = optionalTeacher.get();

            LOGGER.debug("Found Course: {} to unlink from Teacher: {}. unlinking.", course, teacher);

            course.removeTeacher(teacher);
        } else {
            LOGGER.warn("Not found Course and/or Teacher to unlink. Course: {}, Teacher: {}", optionalCourse.isPresent(), optionalTeacher.isPresent());
        }
    }

}
