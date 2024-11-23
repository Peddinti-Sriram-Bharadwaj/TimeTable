package com.sriram9217.timetable.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TimeSlotNotFoundException extends RuntimeException {
    private String msg;
}
