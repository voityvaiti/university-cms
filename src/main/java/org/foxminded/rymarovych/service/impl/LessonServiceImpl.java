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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Date;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class LessonServiceImpl implements LessonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LessonServiceImpl.class);

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository, CourseRepository courseRepository, TeacherRepository teacherRepository, GroupRepository groupRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.groupRepository = groupRepository;
    }


    @Override
    public List<LessonsForDayDto> getAllLessonsForDaysSortedList() {
        LOGGER.debug("Returning sorted list of all Lessons for days,");

        return splitLessonListToLessonsForDayList(lessonRepository.findAll());
    }

    @Override
    public List<LessonsForDayDto> getLessonsForDaysSortedListByGroupAndDateRange(Long groupId, Date startDate, Date endDate) {
        LOGGER.debug("Received request for sorted by number and date LessonsForDays list. " +
                "Params: groupId: {}, startDate: {}, endDate: {}", groupId, startDate, endDate);

        Optional<Group> optionalGroup = groupRepository.findById(groupId);

        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();

            LOGGER.debug("Found Group to get LessonsForDays list: {}", group);

            List<LessonsForDayDto> lessonsForDayDtos = splitLessonListToLessonsForDayList(
                    lessonRepository.findAllByGroupsContainsAndDateBetween(group, startDate, endDate)
            );


            LOGGER.debug("Returning LessonsForDays sorted list: {}, " +
                    "found by groupId: {}, startDate: {} and endDate: {}", lessonsForDayDtos, groupId, startDate, endDate);

            return lessonsForDayDtos;

        } else {
            LOGGER.warn("Not found any Group by ID: {}. Returning an empty ArrayList.", groupId);

            return new ArrayList<>();
        }
    }

    @Override
    public List<LessonsForDayDto> getLessonsForDaysSortedListByTeacherAndDateRange(Long teacherId, Date startDate, Date endDate) {
        LOGGER.debug("Received request for sorted by number and date LessonsForDays list. " +
                "Params: teacherId: {}, startDate: {}, endDate: {}", teacherId, startDate, endDate);

        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);

        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();

            LOGGER.debug("Found Teacher to get LessonsForDays list: {}", teacher);

            List<LessonsForDayDto> lessonsForDayDtos = splitLessonListToLessonsForDayList(
                    lessonRepository.findAllByTeacherAndDateBetween(teacher, startDate, endDate)
            );


            LOGGER.debug("Returning LessonsForDays sorted list: {}, " +
                    "found by teacherId: {}, startDate: {} and endDate: {}", lessonsForDayDtos, teacherId, startDate, endDate);

            return lessonsForDayDtos;

        } else {
            LOGGER.warn("Not found any Teacher by ID: {}. Returning an empty ArrayList.", teacherId);

            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Lesson> findById(Long id) {
        LOGGER.debug("Returning Optional Lesson, found by ID: {}", id);

        return lessonRepository.findById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        LOGGER.debug("Returning all Courses");

        return courseRepository.findAll();
    }

    @Override
    public List<Teacher> getAllTeachers() {
        LOGGER.debug("Returning all Teachers");

        return teacherRepository.findAll();
    }

    @Override
    public List<Group> getAllGroups() {
        LOGGER.debug("Returning all Groups");

        return groupRepository.findAll();
    }

    @Override
    public List<Group> getUnlinkedGroups(Long lessonId) {
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);

        if (optionalLesson.isPresent()) {
            Lesson lesson = optionalLesson.get();

            LOGGER.debug("Found Lesson to get unlinked Groups: {}", lesson);

            List<Group> allGroups = groupRepository.findAll();

            allGroups.removeAll(lesson.getGroups());

            LOGGER.debug("Got list of unlinked Groups: {} to the Lesson: {}", allGroups, lesson);

            return allGroups;

        } else {
            LOGGER.warn("Not found any Lesson by ID: {}. Returning an empty ArrayList.", lessonId);

            return new ArrayList<>();
        }
    }

    @Override
    public Lesson add(Lesson lesson) {

        LOGGER.debug("Saving new Lesson: {}", lesson);

        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson update(Long id, Lesson lesson) {
        LOGGER.debug("Received Lesson to update: {} by ID: {}", lesson, id);

        Optional<Lesson> optionalCurrentLesson = lessonRepository.findById(id);

        if (optionalCurrentLesson.isPresent()) {
            Lesson currentLesson = optionalCurrentLesson.get();

            LOGGER.debug("Found Lesson to update: {} by ID: {}", currentLesson, id);

            currentLesson.setNumber(lesson.getNumber());
            currentLesson.setPlace(lesson.getPlace());
            currentLesson.setDate(lesson.getDate());
            currentLesson.setCourse(lesson.getCourse());
            currentLesson.setTeacher(lesson.getTeacher());

            LOGGER.debug("Saving updated Lesson: {}", currentLesson);

            return lessonRepository.save(currentLesson);

        } else {
            LOGGER.warn("Not found Lesson to update by ID: {}", id);

            return new Lesson();
        }
    }

    @Override
    public void delete(Long id) {
        LOGGER.info("Deleting Lesson by ID: {}", id);

        lessonRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void linkGroup(Long lessonId, Long groupId) {
        LOGGER.debug("Received lessonId: {} to link with groupId: {}", lessonId, groupId);

        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        Optional<Group> optionalGroup = groupRepository.findById(groupId);

        if (optionalLesson.isPresent() && optionalGroup.isPresent()) {
            Lesson lesson = optionalLesson.get();
            Group group = optionalGroup.get();

            LOGGER.debug("Found Lesson: {} to link with Group: {}. Linking.", lesson, group);

            lesson.addGroup(group);
        } else {
            LOGGER.warn("Not found Lesson and/or Group to link. Lesson: {}, Group: {}", optionalLesson.isPresent(), optionalGroup.isPresent());
        }
    }

    @Override
    @Transactional
    public void unlinkGroup(Long lessonId, Long groupId) {
        LOGGER.debug("Received lessonId: {} to unlink from groupId: {}", lessonId, groupId);

        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        Optional<Group> optionalGroup = groupRepository.findById(groupId);

        if (optionalLesson.isPresent() && optionalGroup.isPresent()) {
            Lesson lesson = optionalLesson.get();
            Group group = optionalGroup.get();

            LOGGER.debug("Found Lesson: {} to unlink from Group: {}. Unlinking.", lesson, group);

            lesson.removeGroup(group);
        } else {
            LOGGER.warn("Not found Lesson and/or Group to unlink. Lesson: {}, Group: {}", optionalLesson.isPresent(), optionalGroup.isPresent());
        }
    }

    public List<LessonsForDayDto> splitLessonListToLessonsForDayList(List<Lesson> lessons) {

        return lessons.parallelStream()
                .collect(Collectors.groupingByConcurrent(Lesson::getDate))
                .entrySet().parallelStream()
                .map(e -> {
                    List<Lesson> sortedLessons = e.getValue().stream()
                            .sorted(Comparator.comparing(Lesson::getNumber))
                            .toList();
                    return new LessonsForDayDto(sortedLessons, e.getKey());
                })
                .sorted(Comparator.comparing(LessonsForDayDto::getDate))
                .toList();
    }
}
