package com.hitachi.exam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLot {
    @Id
    @Column(length = 50)
    private String lotId;

    @Column
    private String location;

    @Column
    @Min(value = 1)
    private int capacity;

    @Column
    private int occupiedSpaces;

    @Column
    private double costPerMinute;
}
