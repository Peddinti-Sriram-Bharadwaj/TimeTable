package com.sriram9217.timetable.controller;

import com.sriram9217.timetable.dto.CourseRegisterRequest;
import com.sriram9217.timetable.dto.RegisterRequest;
import com.sriram9217.timetable.dto.WeeklyTimeTableResponse;
import com.sriram9217.timetable.entity.Course;
import com.sriram9217.timetable.exception.CourseNotFoundException;
import com.sriram9217.timetable.exception.StudentNotFoundException;
import com.sriram9217.timetable.exception.TimeSlotNotFoundException;
import com.sriram9217.timetable.helper.RequestInterceptor;
import com.sriram9217.timetable.service.CourseService;
import com.sriram9217.timetable.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    @GetMapping("{id}/timetable")
    public ResponseEntity<?> getWeeklyTimeTable(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        if (!requestInterceptor.validateToken(request, response)) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }

        return studentService.showTimeTable(id);
    }

    @PostMapping("{id}/register-course")
    public ResponseEntity<?> registerCourse(
            @PathVariable Long id,
            @RequestBody CourseRegisterRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        // Validate token
        if (!requestInterceptor.validateToken(httpRequest, httpResponse)) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }

        try {
            // Register the course to the time slot
            studentService.registerCourseToTimeSlot(id, request);
            return ResponseEntity.ok("Course registered to time slot successfully");
        } catch (StudentNotFoundException | CourseNotFoundException | TimeSlotNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("{id}/registered-courses")
    public ResponseEntity<?> getRegisteredCourses(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        if (!requestInterceptor.validateToken(request, response)) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }

        return studentService.getRegisteredCourses(id);
    }

}
