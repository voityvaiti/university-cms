package org.foxminded.rymarovych.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private int number;

    @Column(name = "place")
    private String place;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private ScheduleForDay scheduleForDay;

    @ManyToMany(mappedBy = "lessons")
    private Set<Group> groups = new HashSet<>();

    public Lesson() {
    }

    public Lesson(int number, String place, Course course, Teacher teacher, ScheduleForDay scheduleForDay) {
        this.number = number;
        this.place = place;
        this.course = course;
        this.teacher = teacher;
        this.scheduleForDay = scheduleForDay;
    }

    public Lesson(Long id, int number, String place, Course course, Teacher teacher, ScheduleForDay scheduleForDay) {
        this.id = id;
        this.number = number;
        this.place = place;
        this.course = course;
        this.teacher = teacher;
        this.scheduleForDay = scheduleForDay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public ScheduleForDay getScheduleForDay() {
        return scheduleForDay;
    }

    public void setScheduleForDay(ScheduleForDay scheduleForDay) {
        this.scheduleForDay = scheduleForDay;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", number=" + number +
                ", place='" + place + '\'' +
                ", courseId=" + course.getId() +
                ", teacherId=" + teacher.getId() +
                ", scheduleForDayId=" + scheduleForDay.getId() +
                '}';
    }
}
