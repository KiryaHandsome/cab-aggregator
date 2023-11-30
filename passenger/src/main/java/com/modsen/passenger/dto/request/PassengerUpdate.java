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

    @Size(min = 2, max = 255, message = "passenger.name.size")
    private String name;

    @Size(min = 2, max = 255, message = "passenger.surname.size")
    private String surname;

    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}$", message = "passenger.email.email")
    private String email;

    @Pattern(regexp = "^\\+\\d{12}$", message = "passenger.phone.pattern")
    private String phoneNumber;
}
