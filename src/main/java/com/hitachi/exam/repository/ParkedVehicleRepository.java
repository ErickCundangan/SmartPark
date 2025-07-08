package com.hitachi.exam.repository;

import com.hitachi.exam.entity.ParkedVehicle;
import com.hitachi.exam.entity.ParkingLot;
import com.hitachi.exam.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkedVehicleRepository extends JpaRepository<ParkedVehicle, Integer> {
    List<ParkedVehicle> findByParkingLotAndCheckOutTimeIsNull(ParkingLot parkingLot);
    List<ParkedVehicle> findByCheckOutTimeIsNull();
    Optional<ParkedVehicle> findByVehicleAndCheckOutTimeIsNull(Vehicle vehicle);
}
