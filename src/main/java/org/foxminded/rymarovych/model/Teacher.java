package org.foxminded.rymarovych.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "teachers")
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


    public Teacher() {}

    public Teacher(String firstName, String lastName, String degree) {
        super(firstName, lastName);
        this.degree = degree;
    }

    public Teacher(Long id, String firstName, String lastName, String degree) {
        super(id, firstName, lastName);
        this.degree = degree;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
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
