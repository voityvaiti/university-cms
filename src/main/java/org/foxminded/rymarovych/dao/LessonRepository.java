package org.foxminded.rymarovych.dao;

import org.foxminded.rymarovych.model.Group;
import org.foxminded.rymarovych.model.Lesson;
import org.foxminded.rymarovych.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findAllByGroupsContainsAndDateBetween(Group group, Date startDate, Date endDate);

    List<Lesson> findAllByTeacherAndDateBetween(Teacher teacher, Date startDate, Date endDate);

}
