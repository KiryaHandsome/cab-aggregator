package com.modsen.driver.util;

import com.modsen.driver.model.Driver;
import com.modsen.driver.model.Status;

public class TestEntities {


    public static final Integer JOHN_ID = 1;
    public static final String JOHN_NAME = "John";
    public static final String JOHN_SURNAME = "Doe";
    public static final String JOHN_EMAIL = "john.doe@example.com";
    public static final String JOHN_PHONE_NUMBER = "+375447775566";
    public static final Integer JOHN_TOTAL_RATINGS = 5;
    public static final Float JOHN_AVERAGE_RATING = 4.f;
    public static final Status JOHN_STATUS = Status.AVAILABLE;

    public static Driver johnDoe() {
        return new Driver(JOHN_ID, JOHN_NAME, JOHN_SURNAME, JOHN_EMAIL, JOHN_PHONE_NUMBER, JOHN_STATUS);
    }
}
