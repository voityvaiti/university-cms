package org.foxminded.rymarovych.service.abstractions;

import org.foxminded.rymarovych.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> getAllCoursesList();

    Optional<Course> findById(Long id);

    Course add(Course course);

    Course update(Long id, Course course);

    void delete(Long id);

    void unlinkGroup(Long courseId, Long groupId);

    void unlinkTeacher(Long courseId, Long groupId);

}
