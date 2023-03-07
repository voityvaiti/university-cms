package org.foxminded.rymarovych.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "schedules_for_days")
public class ScheduleForDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day")
    private Date date;

    @OneToMany(mappedBy = "scheduleForDay")
    private List<Lesson> lessons = new ArrayList<>();

    public ScheduleForDay() {
    }

    public ScheduleForDay(Date date) {
        this.date = date;
    }

    public ScheduleForDay(Long id, Date date) {
        this.id = id;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public String toString() {
        return "ScheduleForDay{" +
                "id=" + id +
                ", date=" + date.toString() +
                '}';
    }
}
