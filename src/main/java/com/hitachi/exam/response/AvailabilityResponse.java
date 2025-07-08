package com.hitachi.exam.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityResponse {
    private String lotId;
    private int capacity;
    private int occupiedSpaces;
    private int available;
}
