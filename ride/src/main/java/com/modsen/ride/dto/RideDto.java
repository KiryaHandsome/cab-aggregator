package com.modsen.ride.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideDto {

    private Integer passengerId;
    private Integer driverId;
    private String from;
    private String to;
}
