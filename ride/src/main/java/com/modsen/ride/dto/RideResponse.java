package com.modsen.ride.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideResponse {

    private Integer id;
    private Integer driverId;
    private Integer passengerId;
    private String from;
    private String to;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
}
