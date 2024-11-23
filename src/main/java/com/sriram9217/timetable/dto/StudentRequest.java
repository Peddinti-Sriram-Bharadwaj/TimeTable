package com.sriram9217.timetable.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record StudentRequest(

        @JsonProperty("id")
        long id,

        @NotEmpty
        @NotBlank
        @NotNull
        @JsonProperty("firstName")
        String firstName,

        @JsonProperty("lastName")
        String lastName,

        @NotNull
        @NotEmpty
        @NotBlank
        @JsonProperty("email")
        String email,

        @JsonProperty
        String password

)
{}
