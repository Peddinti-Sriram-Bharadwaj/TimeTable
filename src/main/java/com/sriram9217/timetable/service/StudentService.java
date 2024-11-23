package com.sriram9217.timetable.service;


import com.sriram9217.timetable.dto.LoginRequest;
import com.sriram9217.timetable.dto.RegisterRequest;
import com.sriram9217.timetable.dto.StudentRequest;
import com.sriram9217.timetable.entity.Password;
import com.sriram9217.timetable.entity.Student;
import com.sriram9217.timetable.exception.StudentNotFoundException;
import com.sriram9217.timetable.helper.EncryptionService;
import com.sriram9217.timetable.helper.JWtHelper;
import com.sriram9217.timetable.mapper.StudentMapper;
import com.sriram9217.timetable.repo.PasswordsHolderRepo;
import com.sriram9217.timetable.repo.StudentRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepo studentRepo;
    private final PasswordsHolderRepo passwordsHolderRepo;
    private final StudentMapper studentMapper;
    private final PasswordEncoder passwordEncoder;
    private final EncryptionService encryptionService;
    private final JWtHelper jwtHelper;

    public String registerStudent(RegisterRequest request) {
        Student student = studentMapper.toStudent(request);
        String encryptedPassword = passwordEncoder.encode(request.password());
        Password password = new Password();
        password.setHashedPassword(encryptedPassword);
        password.setStudent(student); // Link PasswordsHolder to Student
        student.setPassword(password); // Link Student to PasswordsHolder
        studentRepo.save(student);
        return "Student created successfully";
    }

    public Student getStudentByEmail(String email) {
        return studentRepo.findByEmail(email).orElseThrow(
                () -> new StudentNotFoundException("Customer with email " + email + " not found")
        );
    }

    public Student getStudentById(Long id) {
        return studentRepo.findById(id).orElseThrow(
                () -> new StudentNotFoundException("Customer with Id " + id + " not found")
        );
    }

    public Password getPasswordById(Long id) {
        return passwordsHolderRepo.findById(id).orElseThrow(
                () -> new StudentNotFoundException("Customer with Id " + id + " not found")
        );
    }

    public ResponseEntity<?> login(LoginRequest request) {
        Student student = getStudentByEmail(request.email());
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }

        Password password = getPasswordById(student.getPassword().getId());
        if (password == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Password holder not found");
        }

        boolean isPasswordValid = encryptionService.validates(request.password(), password.getHashedPassword());
        if (!isPasswordValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password");
        }

        String token = jwtHelper.generateToken(student.getEmail());
        return ResponseEntity.ok(token);
    }





}
