package com.sriram9217.timetable.mapper;


import com.sriram9217.timetable.dto.RegisterRequest;
import com.sriram9217.timetable.dto.StudentRequest;
import com.sriram9217.timetable.entity.Student;
import org.springframework.stereotype.Service;

@Service
public class CourseMapper {

    public Student toCourse(RegisterRequest request){
        return Student.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
    }

}
