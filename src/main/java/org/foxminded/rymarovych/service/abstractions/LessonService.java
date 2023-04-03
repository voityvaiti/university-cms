package org.foxminded.rymarovych.service.abstractions;

import org.foxminded.rymarovych.model.Course;
import org.foxminded.rymarovych.model.Group;
import org.foxminded.rymarovych.model.Lesson;
import org.foxminded.rymarovych.model.Teacher;
import org.foxminded.rymarovych.model.dto.LessonsForDayDto;

import java.util.List;
import java.util.Optional;

public interface LessonService {
    List<LessonsForDayDto> getAllLessonsForDaysSortedList();

    Optional<Lesson> findById(Long id);

    List<Course> getAllCourses();

    List<Teacher> getAllTeachers();

    List<Group> getAllGroups();

    List<Group> getUnlinkedGroups(Long lessonId);

    Lesson add(Lesson lesson);

    Lesson update(Long id, Lesson lesson);

    void delete(Long id);

    void linkGroup(Long lessonId, Long groupId);

    void unlinkGroup(Long lessonId, Long groupId);

}
