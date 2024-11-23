package com.sriram9217.timetable.controller;

import com.sriram9217.timetable.dto.RegisterRequest;
import com.sriram9217.timetable.entity.Course;
import com.sriram9217.timetable.helper.RequestInterceptor;
import com.sriram9217.timetable.service.CourseService;
import com.sriram9217.timetable.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;
    private final CourseService courseService;
    private final RequestInterceptor requestInterceptor;

    public StudentController(StudentService studentService, CourseService courseService, RequestInterceptor requestInterceptor) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.requestInterceptor = requestInterceptor; // Injecting the interceptor
    }

    @PostMapping
    public ResponseEntity<String> registerStudent(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(studentService.registerStudent(request));
    }

    @GetMapping("{id}/courses") // Adjusted endpoint for clarity
    public ResponseEntity<List<Course>> getAllCourses(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        // Manually validate the token
        if (!requestInterceptor.validateToken(request, response)) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }

        // If validation passes, fetch and return all courses
        List<Course> courses = courseService.getAll(); // Assuming getAll() retrieves all courses
        return ResponseEntity.ok(courses);
    }
}
