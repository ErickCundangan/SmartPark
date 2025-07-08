package com.hitachi.exam.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateParkingLotRequest {
    @NotNull(message = "Id is required")
    @NotEmpty(message = "Id is required")
    private String id;
    private String location;

    @Min(value = 1, message = "Parking lot capacity must be greater than 0")
    private int capacity;
    private double costPerMinute;
}
