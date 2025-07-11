package com.hitachi.exam.request;

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
public class CreateVehicleRequest {
    @NotNull(message = "License plate is required")
    @NotEmpty(message = "License plate is required")
    private String licensePlate;
    private String vehicleType;
    private String ownerName;
}
