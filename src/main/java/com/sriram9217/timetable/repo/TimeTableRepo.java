package com.sriram9217.timetable.repo;

import com.sriram9217.timetable.entity.TimeTableEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TimeTableRepo extends JpaRepository<TimeTableEntry, Long> {

    List<TimeTableEntry> findByStudentId(Long studentId);

    @Query("SELECT t FROM TimeTableEntry t WHERE t.student.id = :studentId AND t.timeSlot.id = :timeSlotId")
    Optional<TimeTableEntry> findByStudentIdAndTimeSlotId(@Param("studentId") Long studentId, @Param("timeSlotId") Long timeSlotId);

}
