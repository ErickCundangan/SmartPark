package com.hitachi.exam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkedVehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "lotId")
    private ParkingLot parkingLot;

    @ManyToOne
    @JoinColumn(name = "licensePlate")
    private Vehicle vehicle;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date checkInTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date checkOutTime;
}
