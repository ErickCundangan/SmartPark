package com.hitachi.exam.service;

import com.hitachi.exam.entity.ParkingLot;
import com.hitachi.exam.repository.ParkingLotRepository;
import com.hitachi.exam.request.CreateParkingLotRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ParkingLotService {
    @Autowired
    ParkingLotRepository repository;

    public ParkingLot create(CreateParkingLotRequest request) throws Exception {
        if (repository.findById(request.getId()).isPresent()) {
            throw new Exception("Parking lot already exists");
        }

        return repository.save(new ParkingLot(
                request.getId(),
                request.getLocation(),
                request.getCapacity(),
                0,
                request.getCostPerMinute()
        ));
    }

    public ParkingLot update(ParkingLot parkingLot) throws Exception {
        if (parkingLot.getLotId() == null || parkingLot.getLotId().isEmpty()) {
            throw new Exception("Parking lot not found");
        }

        return repository.save(parkingLot);
    }

    public ParkingLot findById(String id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new Exception("Parking lot not found"));
    }

    public double calculateFee(double costPerMinute, Date checkInTime, Date checkOutTime) {
        long minutes = (checkOutTime.getTime() - checkInTime.getTime()) / 1000 / 60;
        return costPerMinute * minutes;
    }
}
