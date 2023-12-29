package com.modsen.e2e.dto;

import java.time.LocalDateTime;

public record RideDto(
        String id,
        Integer driverId,
        Integer passengerId,
        String from,
        String to,
        Float cost,
        LocalDateTime startTime,
        LocalDateTime finishTime
) {

}