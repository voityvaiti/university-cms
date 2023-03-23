package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.CourseRepository;
import org.foxminded.rymarovych.model.Course;
import org.foxminded.rymarovych.service.abstractions.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
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
}
