package org.foxminded.rymarovych.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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


    public void addCourse(Course course) {
        this.courses.add(course);
        course.getTeachers().add(this);
    }

    public void removeCourse(Course course) {
        this.courses.remove(course);
        course.getTeachers().remove(this);
    }

    public Teacher(Long id, String firstName, String lastName, String degree, List<Lesson> lessons, Set<Course> courses) {
        super(id, firstName, lastName);
        this.degree = degree;
        this.lessons = lessons;
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(degree, teacher.degree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), degree);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                super.toString() +
                ", degree='" + degree + '\'' +
                '}';
    }
}
