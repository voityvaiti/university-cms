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
        Optional<Course> optionalCourse =  courseRepository.findById(courseId);

        if(optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            List<Group> allGroups = groupRepository.findAll();

            allGroups.removeAll(course.getGroups());

            return allGroups;

        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Teacher> getUnlinkedTeachers(Long courseId) {
        Optional<Course> optionalCourse =  courseRepository.findById(courseId);

        if(optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            List<Teacher> allTeachers = teacherRepository.findAll();

            allTeachers.removeAll(course.getTeachers());

            return allTeachers;

        } else {
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
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        Optional<Group> optionalGroup = groupRepository.findById(groupId);

        if(optionalCourse.isPresent() && optionalGroup.isPresent()) {
            optionalCourse.get().addGroup(optionalGroup.get());
        }
    }

    @Override
    @Transactional
    public void unlinkGroup(Long courseId, Long groupId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        Optional<Group> optionalGroup = groupRepository.findById(groupId);

        if(optionalCourse.isPresent() && optionalGroup.isPresent()) {
            optionalCourse.get().removeGroup(optionalGroup.get());
        }
    }

    @Override
    @Transactional
    public void linkTeacher(Long courseId, Long teacherId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);

        if(optionalCourse.isPresent() && optionalTeacher.isPresent()) {
            optionalCourse.get().addTeacher(optionalTeacher.get());
        }
    }

    @Override
    @Transactional
    public void unlinkTeacher(Long courseId, Long teacherId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);

        if(optionalCourse.isPresent() && optionalTeacher.isPresent()) {
            optionalCourse.get().removeTeacher(optionalTeacher.get());
        }
    }

}
