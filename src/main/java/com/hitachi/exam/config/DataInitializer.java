package com.hitachi.exam.config;

import com.hitachi.exam.entity.VehicleType;
import com.hitachi.exam.repository.VehicleTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initVehicleTypes(VehicleTypeRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                VehicleType car = new VehicleType();
                car.setType("car");

                VehicleType motorcycle = new VehicleType();
                motorcycle.setType("motorcycle");

                VehicleType truck = new VehicleType();
                truck.setType("truck");

                repository.save(car);
                repository.save(motorcycle);
                repository.save(truck);

                System.out.println("Init vehicle types done!");
            }
        };
    }
}
