package org.foxminded.rymarovych.service.abstractions;

import org.foxminded.rymarovych.model.Group;
import org.foxminded.rymarovych.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> getAllStudentsList();

    Optional<Student> findById(Long id);

    List<Group> getAllGroups();

    Student add(Student student);

    Student update(Long id, Student student);

    void delete(Long id);

    void linkGroup(Long studentId, Long groupId);

    void  unlinkGroup(Long studentId);
}
