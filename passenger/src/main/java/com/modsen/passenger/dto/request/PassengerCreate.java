package com.modsen.passenger.dto.request;

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

    @NotNull(message = "passenger.name.not_null")
    @NotBlank(message = "passenger.name.not_blank")
    @Size(min = 2, max = 255, message = "passenger.name.size")
    private String name;

    @NotNull(message = "passenger.surname.not_null")
    @NotBlank(message = "passenger.surname.not_blank")
    @Size(min = 2, max = 255, message = "passenger.surname.size")
    private String surname;

    @NotNull(message = "passenger.email.not_null")
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}$", message = "passenger.email.email")
    private String email;

    @NotNull(message = "passenger.phone.not_null")
    @Pattern(regexp = "^\\+\\d{12}$", message = "passenger.phone.pattern")
    private String phoneNumber;
}
