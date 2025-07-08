package com.hitachi.exam.service;

import com.hitachi.exam.entity.Vehicle;
import com.hitachi.exam.entity.VehicleType;
import com.hitachi.exam.repository.VehicleRepository;
import com.hitachi.exam.repository.VehicleTypeRepository;
import com.hitachi.exam.request.CreateVehicleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    VehicleTypeRepository vehicleTypeRepository;

    public Vehicle create(CreateVehicleRequest request) throws Exception {
        if (vehicleRepository.findById(request.getLicensePlate()).isPresent()) {
            throw new Exception("Vehicle already exists");
        }

        VehicleType vehicleType = vehicleTypeRepository.findByType(request.getVehicleType()).orElse(null);

        if (vehicleType == null) {
            throw new Exception("Invalid vehicle type");
        }

        return vehicleRepository.save(new Vehicle(
                request.getLicensePlate(),
                vehicleType,
                request.getOwnerName()
        ));
    }

    public Vehicle findById(String id) throws Exception {
        return vehicleRepository.findById(id).orElseThrow(() -> new Exception("Vehicle not found"));
    }
}
