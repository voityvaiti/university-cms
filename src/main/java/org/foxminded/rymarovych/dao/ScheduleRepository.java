package org.foxminded.rymarovych.dao;

import org.foxminded.rymarovych.model.ScheduleForDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleForDay, Long> {
}
