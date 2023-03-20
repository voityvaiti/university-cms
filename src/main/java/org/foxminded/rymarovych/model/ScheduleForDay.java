package org.foxminded.rymarovych.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "schedules_for_days")
@Data
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

}
