package com.modsen.driver.util;


import com.modsen.driver.dto.event.RideOrdered;
import com.modsen.driver.dto.request.DriverCreate;
import com.modsen.driver.dto.request.DriverUpdate;
import com.modsen.driver.dto.response.DriverResponse;
import com.modsen.driver.dto.response.RatingResponse;
import com.modsen.driver.model.Driver;
import com.modsen.driver.model.Rating;
import com.modsen.driver.model.Status;

public class TestData {

    public static final Integer DRIVER_ID = 1;
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String EMAIL = "email@gmail.com";
    public static final String PHONE_NUMBER = "80294443322";
    public static final String NEW_NAME = "new name";
    public static final String NEW_SURNAME = "new surname";
    public static final String NEW_EMAIL = "newemail@email.com";
    public static final String NEW_PHONE_NUMBER = "778899001122";
    public static final Integer PASSENGER_ID = 13;
    public static final String START_LOCATION = "Minsk";
    public static final String FINISH_LOCATION = "Volkovysk";
    public static final Integer RATING_ID = 51;
    public static final Integer TOTAL_RATINGS = 100;
    public static final Float AVERAGE_RATING = 3.7f;


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

    public static Rating initialRating() {
        return new Rating(null, 0, 0f, defaultDriver());
    }

    public static DriverUpdate newDriverUpdate() {
        return new DriverUpdate(
                NEW_NAME,
                NEW_SURNAME,
                NEW_EMAIL,
                NEW_PHONE_NUMBER,
                Status.BUSY
        );
    }

    public static RideOrdered defaultRideOrdered() {
        return new RideOrdered(PASSENGER_ID, START_LOCATION, FINISH_LOCATION);
    }

    public static Rating defaultRating() {
        return new Rating(RATING_ID, TOTAL_RATINGS, AVERAGE_RATING, defaultDriver());
    }

    public static RatingResponse defaultRatingResponse() {
        return new RatingResponse(AVERAGE_RATING, TOTAL_RATINGS);
    }
}
