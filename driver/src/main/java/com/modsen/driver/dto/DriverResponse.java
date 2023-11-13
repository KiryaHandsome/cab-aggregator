package com.modsen.driver.dto;

import com.modsen.driver.model.Status;

public record DriverResponse(
        Integer id,
        String name,
        String surname,
        String email,
        String phoneNumber,
        Status status
) {

}
