package org.foxminded.rymarovych.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "students")
@NoArgsConstructor
@Getter
@Setter
public class Student extends Person {

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public Student(Long id, String firstName, String lastName, Group group) {
        super(id, firstName, lastName);
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(group, student.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), group);
    }

    @Override
    public String toString() {
        return  "Student{" +
                super.toString() +
                ", groupId=" + group.getId() +
                '}';
    }
}
