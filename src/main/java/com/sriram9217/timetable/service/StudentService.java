package com.sriram9217.timetable.service;


import com.sriram9217.timetable.dto.LoginRequest;
import com.sriram9217.timetable.dto.RegisterRequest;
import com.sriram9217.timetable.dto.StudentRequest;
import com.sriram9217.timetable.entity.PasswordsHolder;
import com.sriram9217.timetable.entity.Student;
import com.sriram9217.timetable.mapper.StudentMapper;
import com.sriram9217.timetable.repo.PasswordsHolderRepo;
import com.sriram9217.timetable.repo.StudentRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepo studentRepo;
    private final PasswordsHolderRepo passwordsHolderRepo;
    private final StudentMapper studentMapper;
    private final PasswordEncoder passwordEncoder;

    public String registerStudent(RegisterRequest request) {
        Student student = studentMapper.toStudent(request);
        String encryptedPassword = passwordEncoder.encode(request.password());
        PasswordsHolder passwordsHolder = new PasswordsHolder();
        passwordsHolder.setHashedPassword(encryptedPassword);
        passwordsHolder.setStudent(student); // Link PasswordsHolder to Student
        student.setPasswordsHolder(passwordsHolder); // Link Student to PasswordsHolder
        studentRepo.save(student);
        return "Student created successfully";
    }


}
