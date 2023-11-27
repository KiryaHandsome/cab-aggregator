package com.modsen.passenger.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerUpdate {

    @Size(min = 2, max = 255, message = "Name size must be between 2 and 255")
    private String name;

    @Size(min = 2, max = 255, message = "Surname size must be between 2 and 255")
    private String surname;

    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^\\+\\d{12}$", message = "Phone number must be in format +123456789012 (exactly 12 digits)")
    private String phoneNumber;
}
