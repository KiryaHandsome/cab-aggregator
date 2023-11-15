package com.modsen.ride.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ride {

    private Integer id;
    private Integer driverId;
    private Integer passengerId;
    private String from;
    private String to;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
}
