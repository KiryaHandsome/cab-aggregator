package com.modsen.passenger.dto;

public record PassengerResponse(
        Integer id,
        String name,
        String surname,
        String email,
        String phoneNumber
) {

}
