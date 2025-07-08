package com.hitachi.exam.controller;

import com.hitachi.exam.entity.ParkedVehicle;
import com.hitachi.exam.entity.ParkingLot;
import com.hitachi.exam.entity.Vehicle;
import com.hitachi.exam.request.CheckOutRequest;
import com.hitachi.exam.request.CreateParkingLotRequest;
import com.hitachi.exam.request.CheckInRequest;
import com.hitachi.exam.response.AvailabilityResponse;
import com.hitachi.exam.response.CheckOutResponse;
import com.hitachi.exam.response.ErrorResponse;
import com.hitachi.exam.service.ParkedVehicleService;
import com.hitachi.exam.service.ParkingLotService;
import com.hitachi.exam.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {
    @Autowired
    ParkingLotService parkingLotService;

    @Autowired
    ParkedVehicleService parkedVehicleService;

    @Autowired
    VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateParkingLotRequest request) {
        try {
            ParkingLot parkingLot = parkingLotService.create(request);
            return ResponseEntity.ok(parkingLot);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
        }
    }

    @GetMapping("/availability")
    public ResponseEntity<?> getAvailability(@RequestParam(value = "id") String id) {
        try {
            ParkingLot parkingLot = parkingLotService.findById(id);

            return ResponseEntity.ok(new AvailabilityResponse(
                    parkingLot.getLotId(),
                    parkingLot.getCapacity(),
                    parkingLot.getOccupiedSpaces(),
                    parkingLot.getCapacity() - parkingLot.getOccupiedSpaces()
            ));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
        }
    }

    @GetMapping("/parked-vehicles")
    public ResponseEntity<?> getParkedVehicles(@RequestParam(value = "id") String id) {
        try {
            ParkingLot parkingLot = parkingLotService.findById(id);

            List<ParkedVehicle> parkedVehicles = parkedVehicleService.findParkedVehicles(parkingLot);

            return ResponseEntity.ok(parkedVehicles.stream().map(ParkedVehicle::getVehicle).collect(Collectors.toList()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
        }
    }

    @PostMapping("/check-in")
    public ResponseEntity<?> checkIn(@RequestBody CheckInRequest request) {
        try {
            ParkingLot parkingLot = parkingLotService.findById(request.getLotId());
            Vehicle vehicle = vehicleService.findById(request.getLicensePlate());

            if (parkingLot.getOccupiedSpaces() >= parkingLot.getCapacity()) {
                throw new Exception("Parking lot is fully occupied");
            }

            ParkedVehicle checkIn = parkedVehicleService.checkIn(parkingLot, vehicle);
            parkingLot.setOccupiedSpaces(parkingLot.getOccupiedSpaces() + 1);
            parkingLotService.update(parkingLot);

            return ResponseEntity.ok(checkIn);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
        }
    }

    @PostMapping("/check-out")
    public ResponseEntity<?> checkOut(@RequestBody CheckOutRequest request) {
        try {
            Vehicle vehicle = vehicleService.findById(request.getLicensePlate());

            ParkedVehicle checkOut = parkedVehicleService.checkOut(vehicle);
            ParkingLot parkingLot = checkOut.getParkingLot();
            parkingLot.setOccupiedSpaces(parkingLot.getOccupiedSpaces() - 1);
            parkingLotService.update(parkingLot);

            double parkingFee = parkingLotService.calculateFee(checkOut.getParkingLot().getCostPerMinute(), checkOut.getCheckInTime(), checkOut.getCheckOutTime());

            return ResponseEntity.ok(new CheckOutResponse(
                    checkOut.getParkingLot().getLotId(),
                    checkOut.getVehicle().getLicensePlate(),
                    checkOut.getCheckInTime(),
                    checkOut.getCheckOutTime(),
                    parkingFee
            ));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
        }
    }
}
