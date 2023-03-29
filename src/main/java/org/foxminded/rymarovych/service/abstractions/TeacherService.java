package org.foxminded.rymarovych.service.abstractions;

import org.foxminded.rymarovych.model.Course;
import org.foxminded.rymarovych.model.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherService {
    List<Teacher> getAllTeachersList();

    Optional<Teacher> findById(Long id);

    List<Course> getUnlinkedCourses(Long teacherId);

    Teacher add(Teacher teacher);

    Teacher update(Long id, Teacher teacher);

    void delete(Long id);

    void linkCourse(Long teacherId, Long courseId);

    void unlinkCourse(Long teacherId, Long courseId);

}
