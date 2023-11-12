package com.modsen.passenger.dto;

public record PassengerResponse(
        Integer id,
        String name,
        String surname,
        String email,
        Float rating,
        String phoneNumber
) {

}
