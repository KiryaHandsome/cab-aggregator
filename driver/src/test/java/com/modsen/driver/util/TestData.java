package com.modsen.driver.util;


import com.modsen.driver.dto.request.DriverCreate;
import com.modsen.driver.dto.request.DriverUpdate;
import com.modsen.driver.dto.response.DriverResponse;
import com.modsen.driver.model.Driver;
import com.modsen.driver.model.Rating;
import com.modsen.driver.model.Status;

public class TestData {

    public static final Integer DRIVER_ID = 1;
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String EMAIL = "email@gmail.com";
    public static final String PHONE_NUMBER = "80294443322";

    public static Driver defaultDriver() {
        return new Driver(
                DRIVER_ID,
                NAME,
                SURNAME,
                EMAIL,
                PHONE_NUMBER,
                Status.OFFLINE
        );
    }

    public static DriverResponse defaultDriverResponse() {
        return new DriverResponse(
                DRIVER_ID,
                NAME,
                SURNAME,
                EMAIL,
                PHONE_NUMBER,
                Status.OFFLINE
        );
    }

    public static DriverUpdate defaultDriverUpdate() {
        return new DriverUpdate(
                NAME,
                SURNAME,
                EMAIL,
                PHONE_NUMBER,
                Status.AVAILABLE
        );
    }

    public static DriverCreate defaultDriverCreate() {
        return new DriverCreate(
                NAME,
                SURNAME,
                EMAIL,
                PHONE_NUMBER
        );
    }

    public static Rating initialRating() {
        return new Rating(null, 0, 0f, defaultDriver());
    }
}
