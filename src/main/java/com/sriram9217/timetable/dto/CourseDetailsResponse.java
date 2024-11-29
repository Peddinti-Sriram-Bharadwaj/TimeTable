package com.sriram9217.timetable.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CourseDetailsResponse(

        @JsonProperty("courseName")
        String courseName,

        @JsonProperty("faculty")
        String faculty,

        @JsonProperty("roomNumber")
        String roomNumber,

        @JsonProperty("specialization")
        String specialization
) {}
