package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.StudentRepository;
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

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @Override
    public List<Student> getAllStudentsList() {
        LOGGER.debug("Returning all students list");

        return studentRepository.findAll();
    }


}
