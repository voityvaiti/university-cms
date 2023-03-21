package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.TeacherRepository;
import org.foxminded.rymarovych.model.Teacher;
import org.foxminded.rymarovych.service.abstractions.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherServiceImpl.class);

    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public List<Teacher> getAllTeachersList() {
        LOGGER.debug("Returning all teachers list");

        return teacherRepository.findAll();
    }

}
