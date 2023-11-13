package com.modsen.driver.dto;

import com.modsen.driver.model.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DriverUpdate(
        @Size(min = 2, max = 255, message = "Name size must be between 2 and 255")
        String name,

        @Size(min = 2, max = 255, message = "Surname size must be between 2 and 255")
        String surname,

        @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
        String email,

        @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
        @Pattern(regexp = "\\d+", message = "Phone number must contain only digits")
        String phoneNumber,
        Status status
) {

}