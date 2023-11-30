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

    @Min(value = 0, message = "score.min")
    @Max(value = 5, message = "score.max")
    private Integer score;
}
