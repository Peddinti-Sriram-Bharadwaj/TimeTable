package com.sriram9217.timetable.service;


import com.sriram9217.timetable.dto.*;
import com.sriram9217.timetable.entity.*;
import com.sriram9217.timetable.exception.CourseNotFoundException;
import com.sriram9217.timetable.exception.StudentNotFoundException;
import com.sriram9217.timetable.exception.TimeSlotNotFoundException;
import com.sriram9217.timetable.helper.EncryptionService;
import com.sriram9217.timetable.helper.JWtHelper;
import com.sriram9217.timetable.mapper.StudentMapper;
import com.sriram9217.timetable.repo.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepo studentRepo;
    private final PasswordsHolderRepo passwordsHolderRepo;
    private final TimeTableRepo timeTableRepo;
    private final TimeSlotRepo timeSlotRepo;
    private final StudentMapper studentMapper;
    private final PasswordEncoder passwordEncoder;
    private final EncryptionService encryptionService;
    private final JWtHelper jwtHelper;
    private final CourseRepo courseRepo;

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

    public ResponseEntity<?> showtimeTable(Long id) {
        // Fetch the student by ID
        Student student = studentRepo.findById(id).orElseThrow(
                () -> new StudentNotFoundException("Student with ID " + id + " not found")
        );

        // Fetch timetable entries for the student
        List<TimeTableEntry> timeTableEntries = timeTableRepo.findByStudentId(id);
        if (timeTableEntries.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Timetable not found");
        }

        // Map timetable entries to WeeklyTimeTableResponse
        WeeklyTimeTableResponse weeklyTimeTable = new WeeklyTimeTableResponse(
                student.getId(),
                timeTableEntries.stream()
                        .collect(Collectors.groupingBy(
                                entry -> entry.getTimeSlot().getDayOfWeek(), // Group by day of the week
                                Collectors.mapping(
                                        entry -> new WeeklyTimeTableResponse.TimeSlotResponse(
                                                entry.getTimeSlot().getStartTime(),
                                                entry.getTimeSlot().getEndTime(),
                                                entry.getCourse().getCourseName() // Get the course name from the entry
                                        ),
                                        Collectors.toList()
                                )
                        ))
                        .entrySet()
                        .stream()
                        .map(e -> new WeeklyTimeTableResponse.DayTimeTable(e.getKey(), e.getValue()))
                        .toList()
        );

        return ResponseEntity.ok(weeklyTimeTable);
    }

    public void registerCourseToTimeSlot(Long studentId, CourseTimeSlotRequest request) {
        // Fetch the student by ID
        Student student = studentRepo.findById(studentId).orElseThrow(
                () -> new StudentNotFoundException("Student with ID " + studentId + " not found")
        );

        // Fetch the course by ID
        Course course = courseRepo.findById(request.courseId()).orElseThrow(
                () -> new CourseNotFoundException("Course with ID " + request.courseId() + " not found")
        );

        // Fetch the time slot by ID
        // Fetch the time slot by ID
        TimeSlot timeSlot = timeSlotRepo.findById(request.timeSlotId()).orElseThrow(
                () -> new TimeSlotNotFoundException()
        );


        // Check if the student already has a course registered in the same time slot
        Optional<TimeTableEntry> existingEntry = timeTableRepo.findByStudentIdAndTimeSlotId(studentId, request.timeSlotId());
        if (existingEntry.isPresent()) {
            throw new IllegalArgumentException("Student already has a course registered in this time slot");
        }

        // Create and save a new timetable entry
        TimeTableEntry newEntry = TimeTableEntry.builder()
                .student(student)
                .course(course)
                .timeSlot(timeSlot)
                .build();

        timeTableRepo.save(newEntry);
    }



}
