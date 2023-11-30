package com.modsen.passenger.util;

import com.modsen.passenger.model.Passenger;

public class TestEntities {

    public static final Integer JOHN_ID = 1;
    public static final String JOHN_NAME = "John";
    public static final String JOHN_SURNAME = "Doe";
    public static final String JOHN_EMAIL = "john.doe@example.com";
    public static final String JOHN_PHONE_NUMBER = "+375441112222";
    public static final Integer JOHN_TOTAL_RATINGS = 5;
    public static final Float JOHN_AVERAGE_RATING = 4.f;

    public static Passenger johnDoe() {
        return new Passenger(
                JOHN_ID,
                JOHN_NAME,
                JOHN_SURNAME,
                JOHN_EMAIL,
                JOHN_PHONE_NUMBER
        );
    }

}
