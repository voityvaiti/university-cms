package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.CourseRepository;
import org.foxminded.rymarovych.model.Course;
import org.foxminded.rymarovych.service.abstractions.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
