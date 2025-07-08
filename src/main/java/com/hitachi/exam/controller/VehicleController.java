package com.hitachi.exam.controller;

import com.hitachi.exam.entity.Vehicle;
import com.hitachi.exam.request.CreateVehicleRequest;
import com.hitachi.exam.response.ErrorResponse;
import com.hitachi.exam.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateVehicleRequest request) {
        try {
            Vehicle vehicle = vehicleService.create(request);
            return ResponseEntity.ok(vehicle);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
        }
    }
}
