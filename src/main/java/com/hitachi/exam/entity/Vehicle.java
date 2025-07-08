package com.hitachi.exam.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    @Pattern(regexp = "^[A-Za-z0-9-]+$")
    private String licensePlate;

    @ManyToOne
    @JoinColumn(name = "vehicleType")
    private VehicleType vehicleType;

    @Column
    @Pattern(regexp = "^[A-Za-z ]+$")
    private String ownerName;
}
