package com.sriram9217.timetable.controller;


import com.sriram9217.timetable.dto.LoginRequest;
import com.sriram9217.timetable.dto.RegisterRequest;
import com.sriram9217.timetable.dto.StudentRequest;
import com.sriram9217.timetable.entity.Student;
import com.sriram9217.timetable.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    @PostMapping
    public ResponseEntity<String> registerStudent(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(studentService.registerStudent(request));
    }

}
