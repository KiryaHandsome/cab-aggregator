package com.modsen.ride.dto.response;

import com.modsen.ride.dto.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponse {

    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private DriverStatus status;
}
