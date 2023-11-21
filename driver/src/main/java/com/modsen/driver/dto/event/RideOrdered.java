package com.modsen.driver.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideOrdered {

    private Integer passengerId;
    private String from;
    private String to;
}
