package com.modsen.driver.dto.request;

import com.modsen.driver.model.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverUpdate {

    @Size(min = 2, max = 255, message = "driver.name.size")
    private String name;

    @Size(min = 2, max = 255, message = "driver.surname.size")
    private String surname;

    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}$", message = "driver.email.email")
    private String email;

    @Pattern(regexp = "^\\+\\d{12}$", message = "driver.phone.pattern")
    private String phoneNumber;
    private Status status;
}