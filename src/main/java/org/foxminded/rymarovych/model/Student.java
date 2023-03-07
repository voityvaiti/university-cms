package org.foxminded.rymarovych.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student extends Person {

    @Column(name = "year")
    private int year;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToMany(cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
    @JoinTable(
            name = "students_courses",
            joinColumns = {@JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "course_id")}
    )
    private Set<Course> courses = new HashSet<>();


    public Student() {}

    public Student(String firstName, String lastName, int year, Group group) {
        super(firstName, lastName);
        this.year = year;
        this.group = group;
    }

    public Student(Long id, String firstName, String lastName, int year, Group group) {
        super(id, firstName, lastName);
        this.year = year;
        this.group = group;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return  "Student{" +
                super.toString() +
                ", year=" + year +
                ", groupId=" + group.getId() +
                '}';
    }
}
