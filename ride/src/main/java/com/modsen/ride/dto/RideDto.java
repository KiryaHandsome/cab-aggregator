package com.modsen.ride.dto;

import com.modsen.ride.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideDto {

    private String id;
    private Integer driverId;
    private Integer passengerId;
    private PaymentStatus paymentStatus;
    private String from;
    private String to;
    private Float cost;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
}
