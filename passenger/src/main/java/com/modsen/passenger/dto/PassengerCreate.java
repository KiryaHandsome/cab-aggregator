package com.modsen.passenger.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PassengerCreate {

    @NotNull(message = "Name must not be null")
    @Size(min = 2, max = 255, message = "Name size must be between 2 and 255")
    private String name;

    @NotNull(message = "Surname must not be null")
    @Size(min = 2, max = 255, message = "Surname size must be between 2 and 255")
    private String surname;

    @NotNull(message = "Email must not be null")
    @NotBlank(message = "Email must not be blank")
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
    private String email;

    @NotNull(message = "Phone number must not be null")
    @NotBlank(message = "Phone number must not be blank")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    @Pattern(regexp = "\\d+", message = "Phone number must contain only digits")
    private String phoneNumber;
}
