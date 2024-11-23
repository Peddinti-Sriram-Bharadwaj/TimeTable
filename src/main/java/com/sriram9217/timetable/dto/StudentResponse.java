package com.sriram9217.timetable.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StudentResponse(

        @JsonProperty("id")
        long id,
        @JsonProperty("firstName")
        String firstName,
        @JsonProperty("lastName")
        String lastName,

        @JsonProperty("email")
        String email
){}
