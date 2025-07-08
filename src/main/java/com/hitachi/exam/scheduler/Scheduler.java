package com.hitachi.exam.scheduler;

import com.hitachi.exam.entity.ParkedVehicle;
import com.hitachi.exam.entity.ParkingLot;
import com.hitachi.exam.service.ParkedVehicleService;
import com.hitachi.exam.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Scheduler {
    @Autowired
    ParkedVehicleService parkedVehicleService;

    @Autowired
    ParkingLotService parkingLotService;

    @Scheduled(cron = "0 * * * * *")
    public void checkOverstayingParkedVehicle() {
        parkedVehicleService.findParkedVehicles().forEach((parkedVehicle -> {
            if (has15MinutesPassed(parkedVehicle.getCheckInTime())) {
                try {
                    ParkedVehicle checkOut = parkedVehicleService.checkOut(parkedVehicle.getVehicle());
                    ParkingLot parkingLot = checkOut.getParkingLot();
                    parkingLot.setOccupiedSpaces(parkingLot.getOccupiedSpaces() - 1);
                    parkingLotService.update(parkingLot);
                } catch (Exception ignored) {}
            }
        }));
    }

    private boolean has15MinutesPassed(Date checkInTime) {
        long fifteenMinutesMillis = 15 * 60 * 1000;
        return new Date().getTime() - checkInTime.getTime() >= fifteenMinutesMillis;
    }
}
