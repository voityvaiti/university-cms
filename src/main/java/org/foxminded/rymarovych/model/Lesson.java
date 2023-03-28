package org.foxminded.rymarovych.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return number == lesson.number && Objects.equals(id, lesson.id) && Objects.equals(place, lesson.place) && Objects.equals(course, lesson.course) && Objects.equals(teacher, lesson.teacher) && Objects.equals(scheduleForDay, lesson.scheduleForDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, place, course, teacher, scheduleForDay);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", number=" + number +
                ", place='" + place + '\'' +
                ", course=" + course +
                ", teacher=" + teacher +
                ", scheduleForDay=" + scheduleForDay +
                '}';
    }
}
