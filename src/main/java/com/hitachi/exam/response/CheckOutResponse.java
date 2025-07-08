package com.hitachi.exam.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckOutResponse {
    private String lotId;
    private String licensePlate;
    private Date checkInTime;
    private Date checkOutTime;
    private double parkingFee;
}
