package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.GroupRepository;
import org.foxminded.rymarovych.dao.StudentRepository;
import org.foxminded.rymarovych.model.Student;
import org.foxminded.rymarovych.service.abstractions.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = StudentServiceImpl.class)
class StudentServiceImplTest {

    @MockBean
    StudentRepository studentRepository;

    @MockBean
    GroupRepository groupRepository;

    @Autowired
    StudentService studentService;

    @Test
    void updateIfStudentFound() {
        Long id = 4L;
        Student currentStudent = new Student(id, "firstname", "lastname", null);
        Student updatedStudent = new Student(id, "updatedFirstname", "updatedLastname", null);

        when(studentRepository.findById(id)).thenReturn(Optional.of(currentStudent));
        when(studentRepository.save(any())).then(returnsFirstArg());

        assertEquals(updatedStudent, studentService.update(id, updatedStudent));

        verify(studentRepository).save(updatedStudent);
    }

    @Test
    void notUpdateIfStudentNotFound() {
        Long id = 4L;
        Student updatedStudent = new Student(id, "updatedFirstname", "updatedLastname", null);

        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        assertNotEquals(updatedStudent, studentService.update(id, updatedStudent));

        verify(studentRepository, never()).save(any());
    }
}