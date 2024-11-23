package com.sriram9217.timetable.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy  = GenerationType.AUTO)
    private Long id;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private PasswordsHolder passwordsHolder;

}
