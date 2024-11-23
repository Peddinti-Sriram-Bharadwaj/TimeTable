package com.sriram9217.timetable.repo;

import com.sriram9217.timetable.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotRepo extends JpaRepository<TimeSlot, Long> {}
