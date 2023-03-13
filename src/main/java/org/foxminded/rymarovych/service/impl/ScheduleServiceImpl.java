package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.ScheduleRepository;
import org.foxminded.rymarovych.service.abstractions.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }
}
