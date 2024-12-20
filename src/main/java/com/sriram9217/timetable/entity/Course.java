package com.sriram9217.timetable.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List; // Use List to store multiple TimeSlot entities
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @ManyToMany(mappedBy = "courses")
    @JsonIgnore
    private Set<Student> students; // Back reference to students enrolled in this course

    @Column(name = "faculty")
    private String faculty;

    @Column(name = "room_no")
    private String roomNo;

    @Column
    private String specialization;

    // Add Many-to-Many relationship with TimeSlot
    @ManyToMany
    @JoinTable(
            name = "course_timeslot", // Join table name
            joinColumns = @JoinColumn(name = "course_id"), // Foreign key for Course
            inverseJoinColumns = @JoinColumn(name = "timeslot_id") // Foreign key for TimeSlot
    )
    private List<TimeSlot> timeSlots; // List of TimeSlot entities
}
