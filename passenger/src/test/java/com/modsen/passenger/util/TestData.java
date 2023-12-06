package com.modsen.passenger.util;

import com.modsen.passenger.dto.request.PassengerCreate;
import com.modsen.passenger.dto.request.PassengerUpdate;
import com.modsen.passenger.dto.response.PassengerResponse;
import com.modsen.passenger.model.Passenger;

public class TestData {

    public static final Integer PASSENGER_ID = 1;
    public static final String NAME = "Some name";
    public static final String SURNAME = "Surname";
    public static final String EMAIL = "email@test.com";
    public static final String PHONE_NUMBER = "+375445556677";

    public static final String NEW_NAME = "Some new name";
    public static final String NEW_SURNAME = "NEW Surname";
    public static final String NEW_EMAIL = "new_email@test.com";
    public static final String NEW_PHONE_NUMBER = "+375294447788";

    public static Passenger defaultPassenger() {
        return new Passenger(
                PASSENGER_ID,
                NAME,
                SURNAME,
                EMAIL,
                PHONE_NUMBER
        );
    }

    public static PassengerCreate defaultPassengerCreate() {
        return new PassengerCreate(
                NAME,
                SURNAME,
                EMAIL,
                PHONE_NUMBER
        );
    }

    public static PassengerCreate newPassengerCreate() {
        return new PassengerCreate(
                NEW_NAME,
                NEW_SURNAME,
                NEW_EMAIL,
                NEW_PHONE_NUMBER
        );
    }

    public static PassengerUpdate newPassengerUpdate() {
        return new PassengerUpdate(
                NEW_NAME,
                NEW_SURNAME,
                NEW_EMAIL,
                NEW_PHONE_NUMBER
        );
    }

    public static Passenger newPassengerWithDefaultId() {
        return new Passenger(
                PASSENGER_ID,
                NEW_NAME,
                NEW_SURNAME,
                NEW_EMAIL,
                NEW_PHONE_NUMBER
        );
    }

    public static PassengerUpdate defaultPassengerUpdate() {
        return new PassengerUpdate(
                NAME,
                SURNAME,
                EMAIL,
                PHONE_NUMBER
        );
    }

    public static PassengerResponse defaultPassengerResponse() {
        return new PassengerResponse(
                PASSENGER_ID,
                NAME,
                SURNAME,
                EMAIL,
                PHONE_NUMBER
        );
    }
}
