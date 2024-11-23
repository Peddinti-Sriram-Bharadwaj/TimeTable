package com.sriram9217.timetable.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentNotFoundException extends RuntimeException {
    private final String msg;

}
