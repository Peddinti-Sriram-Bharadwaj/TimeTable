package com.sriram9217.timetable.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "passwordsHolder")
public class Password{
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private long id;

   @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

   @OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", unique = true)
    private Student student;
}
