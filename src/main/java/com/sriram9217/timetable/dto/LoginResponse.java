package com.sriram9217.timetable.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse(
        @JsonProperty("token")
        String token,

        @JsonProperty("studentId")
        Long studentId
) {}
