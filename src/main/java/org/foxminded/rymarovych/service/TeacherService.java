package org.foxminded.rymarovych.service;

import org.foxminded.rymarovych.dao.TeacherRepository;
import org.foxminded.rymarovych.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public void addTeacher(Teacher teacher) {
        teacherRepository.save(teacher);
    }
}
