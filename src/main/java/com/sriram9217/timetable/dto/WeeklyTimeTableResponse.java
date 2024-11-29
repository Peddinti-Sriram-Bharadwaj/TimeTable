package com.sriram9217.timetable.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record WeeklyTimeTableResponse(
        @JsonProperty("student")
        Long studentId,

        @JsonProperty("weekdays")
        List<DayTimeTable> weekdays
) {
    public static record DayTimeTable(
            @JsonProperty("day")
            String day,

            @JsonProperty("timeslots")
            List<TimeSlotResponse> timeSlots
    ) {}

    public static record TimeSlotResponse(
            @JsonProperty("startTime")
            String startTime,

            @JsonProperty("endTime")
            String endTime,

            @JsonProperty("coursename")
            String courseName
    ) {}
}
