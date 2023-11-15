package com.modsen.driver.dto;

import com.modsen.driver.model.Status;
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
    private Status status;
}
