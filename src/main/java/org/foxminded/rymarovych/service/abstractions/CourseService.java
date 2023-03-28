package org.foxminded.rymarovych.service.abstractions;

import org.foxminded.rymarovych.model.Course;
import org.foxminded.rymarovych.model.Group;
import org.foxminded.rymarovych.model.Teacher;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> getAllCoursesList();

    Optional<Course> findById(Long id);

    List<Group> getUnlinkedGroups(Long courseId);

    List<Teacher> getUnlinkedTeachers(Long courseId);

    Course add(Course course);

    Course update(Long id, Course course);

    void delete(Long id);

    void linkGroup(Long courseId, Long groupId);

    void unlinkGroup(Long courseId, Long groupId);

    void linkTeacher(Long courseId, Long groupId);

    void unlinkTeacher(Long courseId, Long groupId);

}
