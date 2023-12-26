package com.modsen.e2e.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequest {

    private Integer passengerId;
    private String from;
    private String to;
}
