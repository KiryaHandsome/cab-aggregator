package com.modsen.driver.util;


import com.modsen.driver.dto.request.DriverCreate;
import com.modsen.driver.dto.request.DriverUpdate;
import com.modsen.driver.dto.response.DriverResponse;
import com.modsen.driver.model.Driver;
import com.modsen.driver.model.Status;

public class TestData {

    public static final Integer DRIVER_ID = 1;
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String EMAIL = "email@gmail.com";
    public static final String PHONE_NUMBER = "80294443322";
    public static final Status STATUS = Status.OFFLINE;
    public static final String NEW_NAME = "new name";
    public static final String NEW_SURNAME = "new surname";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PHONE_NUMBER = "+375447771122";
    public static final Status NEW_STATUS = Status.BUSY;
    public static final Integer PASSENGER_ID = 13;
    public static final String START_LOCATION = "Minsk";
    public static final String FINISH_LOCATION = "Volkovysk";

    public static Driver defaultDriver() {
        return new Driver(
                DRIVER_ID,
                NAME,
                SURNAME,
                EMAIL,
                PHONE_NUMBER,
                STATUS
        );
    }

    public static DriverResponse defaultDriverResponse() {
        return new DriverResponse(
                DRIVER_ID,
                NAME,
                SURNAME,
                EMAIL,
                PHONE_NUMBER,
                STATUS
        );
    }

    public static DriverUpdate defaultDriverUpdate() {
        return new DriverUpdate(
                NAME,
                SURNAME,
                EMAIL,
                PHONE_NUMBER,
                STATUS
        );
    }

    public static DriverUpdate nullDriverUpdate() {
        return new DriverUpdate(
                null,
                null,
                null,
                null,
                null
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

    public static DriverUpdate newDriverUpdate() {
        return new DriverUpdate(
                NEW_NAME,
                NEW_SURNAME,
                NEW_EMAIL,
                NEW_PHONE_NUMBER,
                NEW_STATUS
        );
    }

    public static DriverCreate newDriverCreate() {
        return new DriverCreate(
                NEW_NAME,
                NEW_SURNAME,
                NEW_EMAIL,
                NEW_PHONE_NUMBER
        );
    }
}
