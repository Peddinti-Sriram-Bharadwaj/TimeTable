package com.sriram9217.timetable.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CourseTimeSlotRequest(
        @JsonProperty("courseId") Long courseId,
        @JsonProperty("timeSlotId") Long timeSlotId
) {}
