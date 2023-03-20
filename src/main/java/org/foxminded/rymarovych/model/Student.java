package org.foxminded.rymarovych.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    public String toString() {
        return  "Student{" +
                super.toString() +
                ", groupId=" + group.getId() +
                '}';
    }
}
