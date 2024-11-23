package com.sriram9217.timetable.service;

import com.sriram9217.timetable.entity.Course;
import com.sriram9217.timetable.repo.CourseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepo courseRepo; // Marked as final for proper injection

    public List<Course> getAll() {
        return courseRepo.findAllByOrderByIdAsc(); // This will now work correctly
    }
}
