package com.modsen.e2e.dto;

public record RideRequest(Integer passengerId,
                          String from,
                          String to) {

}
