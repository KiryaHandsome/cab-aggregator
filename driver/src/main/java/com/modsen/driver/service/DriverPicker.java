package com.modsen.driver.service;


import com.modsen.driver.dto.event.RideOrdered;
import com.modsen.driver.dto.response.DriverResponse;

public interface DriverPicker {

    DriverResponse pickAvailable(RideOrdered rideOrdered);
}
