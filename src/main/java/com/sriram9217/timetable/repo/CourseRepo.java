package com.sriram9217.timetable.repo;


import com.sriram9217.timetable.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepo extends JpaRepository<Course, Long> {
    List<Course> findAllByOrderByIdAsc();
}
