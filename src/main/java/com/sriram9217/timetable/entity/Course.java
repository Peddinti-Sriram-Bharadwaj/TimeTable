package com.sriram9217.timetable.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    // Other course properties can go here

    @ManyToMany(mappedBy = "courses")
    @JsonIgnore
    private Set<Student> students; // Back reference to students enrolled in this course
}
