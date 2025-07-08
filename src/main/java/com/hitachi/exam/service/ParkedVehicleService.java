package com.hitachi.exam.service;

import com.hitachi.exam.entity.ParkedVehicle;
import com.hitachi.exam.entity.ParkingLot;
import com.hitachi.exam.entity.Vehicle;
import com.hitachi.exam.repository.ParkedVehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ParkedVehicleService {
    @Autowired
    ParkedVehicleRepository repository;

    public List<ParkedVehicle> findParkedVehicles(ParkingLot parkingLot) {
        return repository.findByParkingLotAndCheckOutTimeIsNull(parkingLot);
    }

    public List<ParkedVehicle> findParkedVehicles() {
        return repository.findByCheckOutTimeIsNull();
    }

    public ParkedVehicle checkIn(ParkingLot parkingLot, Vehicle vehicle) throws Exception {
        ParkedVehicle probe = new ParkedVehicle();
        probe.setVehicle(vehicle);
        boolean isParked = repository.findByVehicleAndCheckOutTimeIsNull(vehicle).isPresent();

        if (isParked) {
            throw new Exception("Vehicle already parked");
        }

        return repository.save(new ParkedVehicle(null, parkingLot, vehicle, null, null));
    }

    public ParkedVehicle checkOut(Vehicle vehicle) throws Exception {
        ParkedVehicle checkOut = repository.findByVehicleAndCheckOutTimeIsNull(vehicle)
                .orElseThrow(() -> new Exception("Vehicle not yet parked"));

        checkOut.setCheckOutTime(new Date());
        return repository.save(checkOut);
    }
}
