package com.modsen.driver.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreRequest {

    @Min(value = 0, message = "Score must be more than or equal to 0")
    @Max(value = 5, message = "Score must be less than or equal to 5")
    private Integer score;
}
