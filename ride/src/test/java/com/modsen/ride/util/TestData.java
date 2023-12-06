package com.modsen.ride.util;

import com.modsen.ride.dto.RideDto;
import com.modsen.ride.dto.RideStart;
import com.modsen.ride.dto.request.RideRequest;
import com.modsen.ride.dto.response.WaitingRideResponse;
import com.modsen.ride.model.PaymentStatus;
import com.modsen.ride.model.Ride;
import com.modsen.ride.model.WaitingRide;

import java.time.LocalDateTime;

public class TestData {

    public static final Integer PASSENGER_ID = 5;
    public static final String RIDE_ID = "12jla2mfadsjf";
    public static final String WAITING_RIDE_ID = "asdjfklh12893llafl";
    public static final String START_LOCATION = "start";
    public static final String FINISH_LOCATION = "finish";
    public static final Integer DRIVER_ID = 52;
    public static final LocalDateTime START_TIME = LocalDateTime.now();
    public static final LocalDateTime FINISH_TIME = START_TIME.plusHours(1);
    public static final Float COST = 718.56f;

    public static RideRequest rideRequest() {
        return new RideRequest(PASSENGER_ID, START_LOCATION, FINISH_LOCATION);
    }

    public static WaitingRide defaultWaitingRide() {
        return new WaitingRide(
                WAITING_RIDE_ID,
                PASSENGER_ID,
                START_LOCATION,
                FINISH_LOCATION
        );
    }

    public static Ride defaultRide() {
        return new Ride(
                RIDE_ID,
                DRIVER_ID,
                PASSENGER_ID,
                START_LOCATION,
                FINISH_LOCATION,
                COST,
                PaymentStatus.WAITING,
                START_TIME,
                null
        );
    }

    public static RideDto defaultRideDto() {
        return new RideDto(
                RIDE_ID,
                DRIVER_ID,
                PASSENGER_ID,
                START_LOCATION,
                FINISH_LOCATION,
                COST,
                START_TIME,
                FINISH_TIME
        );
    }

    public static WaitingRideResponse defaultWaitingRideResponse() {
        return new WaitingRideResponse(
                WAITING_RIDE_ID,
                PASSENGER_ID,
                START_LOCATION,
                FINISH_LOCATION
        );
    }

    public static RideStart rideStart() {
        return new RideStart(DRIVER_ID);
    }
}
