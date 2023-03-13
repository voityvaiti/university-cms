package org.foxminded.rymarovych.model;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student extends Person {

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public Student() {}

    public Student(String firstName, String lastName, Group group) {
        super(firstName, lastName);
        this.group = group;
    }

    public Student(Long id, String firstName, String lastName, Group group) {
        super(id, firstName, lastName);
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
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
