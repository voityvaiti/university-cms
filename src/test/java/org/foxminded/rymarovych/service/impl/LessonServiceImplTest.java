package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.CourseRepository;
import org.foxminded.rymarovych.dao.GroupRepository;
import org.foxminded.rymarovych.dao.LessonRepository;
import org.foxminded.rymarovych.dao.TeacherRepository;
import org.foxminded.rymarovych.model.Course;
import org.foxminded.rymarovych.model.Group;
import org.foxminded.rymarovych.model.Lesson;
import org.foxminded.rymarovych.model.Teacher;
import org.foxminded.rymarovych.model.dto.LessonsForDayDto;
import org.foxminded.rymarovych.service.abstractions.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = LessonServiceImpl.class)
class LessonServiceImplTest {

    @MockBean
    LessonRepository lessonRepository;
    @MockBean
    CourseRepository courseRepository;
    @MockBean
    TeacherRepository teacherRepository;
    @MockBean
    GroupRepository groupRepository;

    @Autowired
    LessonService lessonService;


    private Lesson lesson;

    @BeforeEach
    void setUp() throws ParseException {

        Course course = new Course(3L, "coursename", null, null, null);
        Teacher teacher = new Teacher(12L, "firstname", "lastname", "degree", null, null);

        lesson = new Lesson(8L, 3, "place", new SimpleDateFormat("yyyyMMdd").parse("20200424"), course, teacher, null);
    }

    @Test
    void getUnlinkedGroups() {

        Group group1 = new Group(1L, "name1", "spec1", 1, null, null, null);
        Group group2 = new Group(2L, "name2", "spec2", 2, null, null, null);
        Group group3 = new Group(3L, "name3", "spec3", 3, null, null, null);
        Group group4 = new Group(4L, "name4", "spec4", 4, null, null, null);

        lesson.setGroups(new HashSet<>(List.of(group1, group3)));

        List<Group> expected = List.of(group2, group4);

        when(lessonService.findById(lesson.getId())).thenReturn(Optional.of(lesson));
        when(groupRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(group1, group2, group3, group4)));

        assertEquals(expected, lessonService.getUnlinkedGroups(lesson.getId()));
    }

    @Test
    void updateIfLessonFound() throws ParseException {

        Lesson updatedLesson = new Lesson(null, 6, "updatedplace",
                new SimpleDateFormat("yyyyMMdd").parse("20211027"), null, null, null);

        Lesson expected = new Lesson(lesson.getId(), 6, "updatedplace",
                new SimpleDateFormat("yyyyMMdd").parse("20211027"), null, null, null);

        when(lessonRepository.findById(lesson.getId())).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(any())).then(returnsFirstArg());

        assertEquals(expected, lessonService.update(lesson.getId(), updatedLesson));

        verify(lessonRepository).save(expected);

    }

    @Test
    void notUpdateIfLessonNotFound() throws ParseException {

        Lesson updatedLesson = new Lesson(null, 6, "updatedplace",
                new SimpleDateFormat("yyyyMMdd").parse("20211027"), null, null, null);

        Lesson expected = new Lesson(lesson.getId(), 6, "updatedplace",
                new SimpleDateFormat("yyyyMMdd").parse("20211027"), null, null, null);

        when(lessonRepository.findById(updatedLesson.getId())).thenReturn(Optional.empty());

        assertNotEquals(expected, lessonService.update(lesson.getId(), updatedLesson));

        verify(courseRepository, never()).save(any());

    }

    @Test
    void splitLessonListToLessonsForDayList() throws ParseException {

        Date date1 = new SimpleDateFormat("yyyyMMdd").parse("20211027");
        Date date2 = new SimpleDateFormat("yyyyMMdd").parse("20211029");

        Lesson lesson1 = new Lesson();
        lesson1.setNumber(3);
        lesson1.setDate(date1);

        Lesson lesson2 = new Lesson();
        lesson2.setNumber(4);
        lesson2.setDate(date1);

        Lesson lesson3 = new Lesson();
        lesson3.setNumber(1);
        lesson3.setDate(date2);

        Lesson lesson4 = new Lesson();
        lesson4.setNumber(2);
        lesson4.setDate(date2);

        List<Lesson> messedInputList = Arrays.asList(lesson2, lesson3, lesson4, lesson1);

        List<LessonsForDayDto> expected = Arrays.asList(
                new LessonsForDayDto(Arrays.asList(lesson1, lesson2), date1),
                new LessonsForDayDto(Arrays.asList(lesson3, lesson4), date2)
        );

        assertEquals(expected, lessonService.splitLessonListToLessonsForDayList(messedInputList));
    }
}