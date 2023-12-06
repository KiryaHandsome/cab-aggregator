package com.modsen.payment.util;

import com.modsen.payment.dto.RideInfo;

import java.time.LocalDateTime;

public class TestData {

    public static final String RIDE_ID = "someRideId";
    public static final Integer DRIVER_ID = 4;
    public static final Integer PASSENGER_ID = 4;
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final Float COST = 33.9f;

    public static RideInfo defaultRideInfo() {
        return new RideInfo(
                RIDE_ID,
                DRIVER_ID,
                PASSENGER_ID,
                FROM,
                TO,
                COST,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10)
        );
    }
}
