package org.foxminded.rymarovych.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "teachers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Teacher extends Person {

    @Column(name = "degree")
    private String degree;


    @OneToMany(mappedBy = "teacher")
    private List<Lesson> lessons = new ArrayList<>();

    @ManyToMany(cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
    @JoinTable(
            name = "teachers_courses",
            joinColumns = {@JoinColumn(name = "teacher_id")},
            inverseJoinColumns = {@JoinColumn(name = "course_id")}

    )
    private Set<Course> courses = new HashSet<>();

    public Teacher(Long id, String firstName, String lastName, String degree, List<Lesson> lessons, Set<Course> courses) {
        super(id, firstName, lastName);
        this.degree = degree;
        this.lessons = lessons;
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                super.toString() +
                ", degree='" + degree + '\'' +
                '}';
    }
}
