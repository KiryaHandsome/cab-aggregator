package com.modsen.driver.service;


import com.modsen.driver.dto.event.RideOrdered;

public interface DriverNotifier {

    void notifyAvailableDrivers(RideOrdered rideOrdered);
}
