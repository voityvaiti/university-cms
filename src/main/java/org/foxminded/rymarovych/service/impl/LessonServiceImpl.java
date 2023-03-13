package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.LessonRepository;
import org.foxminded.rymarovych.service.abstractions.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }
}
