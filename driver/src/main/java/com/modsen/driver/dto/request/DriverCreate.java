package com.modsen.driver.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverCreate {

    @NotNull(message = "driver.name.not_null")
    @NotBlank(message = "driver.name.not_blank")
    @Size(min = 2, max = 255, message = "driver.name.size")
    private String name;

    @NotNull(message = "driver.surname.not_null")
    @NotBlank(message = "driver.surname.not_blank")
    @Size(min = 2, max = 255, message = "driver.surname.size")
    private String surname;

    @NotNull(message = "driver.email.not_null")
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}$", message = "driver.email.email")
    private String email;

    @NotNull(message = "driver.phone.not_null")
    @NotBlank(message = "driver.phone.not_blank")
    @Pattern(regexp = "^\\+\\d{12}$", message = "driver.phone.pattern")
    private String phoneNumber;
}