package com.modsen.ride.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequest {

    @Min(value = 1, message = "ride.passenger_id")
    private Integer passengerId;

    @NotBlank(message = "Start location(from) must not be blank")
    private String from;

    @NotBlank(message = "Finish location(to) must not be blank")
    private String to;
}
