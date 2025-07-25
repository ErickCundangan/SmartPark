package com.hitachi.exam.repository;

import com.hitachi.exam.entity.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, Integer> {
    Optional<VehicleType> findByType(String type);
}
