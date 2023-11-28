package com.modsen.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideInfo {

    private String id;
    private Integer driverId;
    private Integer passengerId;
    private String from;
    private String to;
    private Float cost;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
}