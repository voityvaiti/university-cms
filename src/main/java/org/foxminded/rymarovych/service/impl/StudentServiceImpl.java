package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.GroupRepository;
import org.foxminded.rymarovych.dao.StudentRepository;
import org.foxminded.rymarovych.model.Group;
import org.foxminded.rymarovych.model.Student;
import org.foxminded.rymarovych.service.abstractions.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;

    private final GroupRepository groupRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }


    @Override
    public List<Student> getAllStudentsList() {
        LOGGER.debug("Returning all students list");

        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> findById(Long id) {
        LOGGER.debug("Returning Optional Student, found by ID: {}", id);

        return studentRepository.findById(id);
    }

    @Override
    public List<Group> getAllGroups() {
        LOGGER.debug("Returning all Groups");

        return groupRepository.findAll();
    }

    @Override
    public Student add(Student student) {
        LOGGER.debug("Saving new Student: {}", student);

        return studentRepository.save(student);
    }

    @Override
    public Student update(Long id, Student student) {
        LOGGER.debug("Received Student to update: {} by ID: {}", student, id);

        Optional<Student> optionalCurrentStudent = studentRepository.findById(id);

        if(optionalCurrentStudent.isPresent()) {
            Student currentStudent = optionalCurrentStudent.get();

            LOGGER.debug("Found Student to update: {} by ID: {}", currentStudent, id);

            currentStudent.setFirstName(student.getFirstName());
            currentStudent.setLastName(student.getLastName());
            currentStudent.setGroup(student.getGroup());

            LOGGER.debug("Saving updated Student: {}", currentStudent);

            return studentRepository.save(currentStudent);

        } else {
            LOGGER.warn("Not found Student to update by ID: {}", id);

            return new Student();
        }
    }

    @Override
    public void delete(Long id) {
        LOGGER.info("Deleting Student by ID: {}", id);

        studentRepository.deleteById(id);
    }

}
