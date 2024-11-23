package com.sriram9217.timetable.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record LoginRequest(

        @NotNull
                @JsonProperty("email")
                @Email
        String email,

        @NotNull
                @NotEmpty
                @NotBlank
                @Size(min = 6, max = 12)
                @JsonProperty("password")
        String password) {



}
