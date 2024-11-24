package com.sriram9217.timetable.controller;

import com.sriram9217.timetable.dto.LoginRequest;
import com.sriram9217.timetable.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {
    private final StudentService studentService;

    public LoginController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody @Valid LoginRequest request){
        System.out.println("recieved login request: " + request);
        return ResponseEntity.ok(studentService.login(request));
    }



}
