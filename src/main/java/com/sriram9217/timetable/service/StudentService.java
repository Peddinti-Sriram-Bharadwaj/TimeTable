package com.sriram9217.timetable.service;


import com.sriram9217.timetable.dto.*;
import com.sriram9217.timetable.entity.*;
import com.sriram9217.timetable.exception.CourseNotFoundException;
import com.sriram9217.timetable.exception.StudentNotFoundException;
import com.sriram9217.timetable.exception.TimeSlotNotFoundException;
import com.sriram9217.timetable.dto.CourseDetailsResponse;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepo studentRepo;
    private final PasswordsHolderRepo passwordsHolderRepo;
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

    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        Student student = getStudentByEmail(request.email());
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Password password = getPasswordById(student.getPassword().getId());
        if (password == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        boolean isPasswordValid = encryptionService.validates(request.password(), password.getHashedPassword());
        if (!isPasswordValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Generate token
        String token = jwtHelper.generateToken(student.getEmail());

        // Create response object with token and student ID
        LoginResponse loginResponse = new LoginResponse(token, student.getId());

        // Return response
        return ResponseEntity.ok(loginResponse);
    }



    public ResponseEntity<?> showTimeTable(Long studentId) {
        // Fetch the student by ID
        Student student = studentRepo.findById(studentId).orElseThrow(
                () -> new StudentNotFoundException("Student with ID " + studentId + " not found")
        );

        // Fetch all courses the student is enrolled in
        Set<Course> enrolledCourses = student.getCourses();
        if (enrolledCourses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student is not enrolled in any courses");
        }

        // Initialize a list to hold the DayTimeTable data for the response
        Map<String, List<WeeklyTimeTableResponse.TimeSlotResponse>> groupedByDay = new TreeMap<>(); // TreeMap to keep days in order (Mon, Tue, Wed, etc.)

        // Group courses by the day of the week and time slot information
        for (Course course : enrolledCourses) {
            for (TimeSlot timeSlot : course.getTimeSlots()) {
                String dayOfWeek = timeSlot.getDayOfWeek(); // Assume dayOfWeek is in a standard format like "Monday", "Tuesday", etc.
                WeeklyTimeTableResponse.TimeSlotResponse timeSlotResponse = new WeeklyTimeTableResponse.TimeSlotResponse(
                        timeSlot.getStartTime(),
                        timeSlot.getEndTime(),
                        course.getCourseName() // Get the course name for each time slot
                );

                // Add the time slot to the grouped map, by day of the week
                groupedByDay
                        .computeIfAbsent(dayOfWeek, k -> new ArrayList<>()) // Initialize list if absent
                        .add(timeSlotResponse);
            }
        }

        // Prepare the DayTimeTable list from the grouped data
        List<WeeklyTimeTableResponse.DayTimeTable> weeklyTimeTable = new ArrayList<>();
        for (Map.Entry<String, List<WeeklyTimeTableResponse.TimeSlotResponse>> entry : groupedByDay.entrySet()) {
            WeeklyTimeTableResponse.DayTimeTable dayTimeTable = new WeeklyTimeTableResponse.DayTimeTable(
                    entry.getKey(), // Day of the week (e.g., "Monday")
                    entry.getValue() // List of time slots for that day
            );
            weeklyTimeTable.add(dayTimeTable);
        }

        // Create the final response object
        WeeklyTimeTableResponse response = new WeeklyTimeTableResponse(
                student.getId(),
                weeklyTimeTable
        );

        return ResponseEntity.ok(response);
    }




    public void registerCourseToTimeSlot(Long studentId, CourseRegisterRequest request) {
        // Fetch the student by ID
        Student student = studentRepo.findById(studentId).orElseThrow(
                () -> new StudentNotFoundException("Student with ID " + studentId + " not found")
        );

        // Fetch the course by ID
        Course course = courseRepo.findById(request.courseId()).orElseThrow(
                () -> new CourseNotFoundException("Course with ID " + request.courseId() + " not found")
        );

        // Check if the course has available time slots
        List<TimeSlot> availableTimeSlots = course.getTimeSlots();
        if (availableTimeSlots.isEmpty()) {
            throw new IllegalArgumentException("The course has no time slots assigned");
        }

        // Add the course to the student's list of enrolled courses
        student.getCourses().add(course); // Assuming `getCourses()` returns a Set<Course>

        // Register the student for all time slots assigned to the course
        studentRepo.save(student);
    }

    public ResponseEntity<?> getRegisteredCourses(Long studentId) {
        // Fetch the student by ID
        Student student = studentRepo.findById(studentId).orElseThrow(
                () -> new StudentNotFoundException("Student with ID " + studentId + " not found")
        );

        // Fetch the courses the student is enrolled in
        Set<Course> enrolledCourses = student.getCourses();
        if (enrolledCourses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student is not registered for any courses");
        }

        // Map the enrolled courses to CourseDetailsResponse DTO
        List<CourseDetailsResponse> courseDetailsResponses = enrolledCourses.stream()
                .map(course -> new CourseDetailsResponse(
                        course.getCourseName(),
                        course.getFaculty(),
                        course.getRoomNo(),
                        course.getSpecialization()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(courseDetailsResponses);
    }




}
