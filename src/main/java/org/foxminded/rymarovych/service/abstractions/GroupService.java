package org.foxminded.rymarovych.service.abstractions;

import org.foxminded.rymarovych.model.Course;
import org.foxminded.rymarovych.model.Group;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    List<Group> getAllGroupsList();

    Optional<Group> findById(Long id);

    List<Course> getUnlinkedCourses(Long groupId);

    Group add(Group group);

    Group update(Long id, Group group);

    void delete(Long id);

    void linkCourse(Long groupId, Long courseId);

    void unlinkCourse(Long groupId, Long courseId);

}
