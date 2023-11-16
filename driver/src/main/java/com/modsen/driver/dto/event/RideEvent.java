package com.modsen.driver.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideEvent {

    private Integer passengerId;
    private Integer driverId;
    private String from;
    private String to;
}
