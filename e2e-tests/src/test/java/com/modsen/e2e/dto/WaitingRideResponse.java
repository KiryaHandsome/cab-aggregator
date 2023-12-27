package com.modsen.e2e.dto;


public record WaitingRideResponse(String id,
                                  Integer passengerId,
                                  String from,
                                  String to) {

}
