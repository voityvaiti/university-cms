package org.foxminded.rymarovych.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "schedules_for_days")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleForDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day")
    private Date date;

    @OneToMany(mappedBy = "scheduleForDay")
    private List<Lesson> lessons = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleForDay that = (ScheduleForDay) o;
        return Objects.equals(id, that.id) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date);
    }

    @Override
    public String toString() {
        return "ScheduleForDay{" +
                "id=" + id +
                ", date=" + date +
                '}';
    }
}
