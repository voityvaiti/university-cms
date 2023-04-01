package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.CourseRepository;
import org.foxminded.rymarovych.dao.TeacherRepository;
import org.foxminded.rymarovych.model.Course;
import org.foxminded.rymarovych.model.Teacher;
import org.foxminded.rymarovych.service.abstractions.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherServiceImpl.class);

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository, CourseRepository courseRepository) {
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Teacher> getAllTeachersList() {
        LOGGER.debug("Returning all teachers list");

        return teacherRepository.findAll();
    }

    @Override
    public Optional<Teacher> findById(Long id) {
        LOGGER.debug("Returning Optional Teacher, found by ID: {}", id);

        return teacherRepository.findById(id);
    }

    @Override
    public List<Course> getUnlinkedCourses(Long teacherId) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);

        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();

            LOGGER.debug("Found Teacher to get unlinked Courses: {}", teacher);

            List<Course> allCourses = courseRepository.findAll();
            allCourses.removeAll(teacher.getCourses());

            LOGGER.debug("Got list of unlinked Courses: {} to the Teacher: {}", allCourses, teacher);

            return allCourses;

        } else {
            LOGGER.warn("Not found any Teacher by ID: {}. Returning an empty ArrayList.", teacherId);

            return new ArrayList<>();
        }
    }

    @Override
    public Teacher add(Teacher teacher) {

        LOGGER.debug("Saving new Teacher: {}", teacher);

        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher update(Long id, Teacher teacher) {
        LOGGER.debug("Received Teacher to update: {} by ID: {}", teacher, id);

        Optional<Teacher> optionalCurrentTeacher = teacherRepository.findById(id);

        if (optionalCurrentTeacher.isPresent()) {
            Teacher currentTeacher = optionalCurrentTeacher.get();

            LOGGER.debug("Found Teacher to update: {} by ID: {}", currentTeacher, id);

            currentTeacher.setFirstName(teacher.getFirstName());
            currentTeacher.setLastName(teacher.getLastName());
            currentTeacher.setDegree(teacher.getDegree());

            LOGGER.debug("Saving updated Teacher: {}", currentTeacher);

            return teacherRepository.save(currentTeacher);

        } else {
            LOGGER.warn("Not found Teacher to update by ID: {}", id);

            return new Teacher();
        }
    }

    @Override
    public void delete(Long id) {
        LOGGER.info("Deleting Teacher by ID: {}", id);

        teacherRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void linkCourse(Long teacherId, Long courseId) {
        LOGGER.debug("Received teacherId: {} to link with courseId: {}", teacherId, courseId);

        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalTeacher.isPresent() && optionalCourse.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            Course course = optionalCourse.get();

            LOGGER.debug("Found Teacher: {} to link with Course: {}. Linking.", teacher, course);

            teacher.addCourse(course);

        } else {
            LOGGER.warn("Not found Teacher and/or Course to link. Teacher: {}, Course: {}", optionalTeacher.isPresent(), optionalCourse.isPresent());
        }

    }

    @Override
    @Transactional
    public void unlinkCourse(Long teacherId, Long courseId) {

        LOGGER.debug("Received teacherId: {} to unlink with courseId: {}", teacherId, courseId);

        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalTeacher.isPresent() && optionalCourse.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            Course course = optionalCourse.get();

            LOGGER.debug("Found Teacher: {} to unlink from Course: {}. Unlinking.", teacher, course);

            teacher.removeCourse(course);

        } else {
            LOGGER.warn("Not found Teacher and/or Course to unlink. Teacher: {}, Course: {}", optionalTeacher.isPresent(), optionalCourse.isPresent());
        }
    }

}
