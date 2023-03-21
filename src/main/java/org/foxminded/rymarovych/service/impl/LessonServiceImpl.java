package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.LessonRepository;
import org.foxminded.rymarovych.service.abstractions.LessonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonServiceImpl implements LessonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LessonServiceImpl.class);

    private final LessonRepository lessonRepository;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }
}
