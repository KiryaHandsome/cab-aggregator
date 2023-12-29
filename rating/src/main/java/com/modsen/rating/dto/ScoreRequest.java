package com.modsen.rating.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreRequest {

    @Max(value = 5, message = "score.max")
    @Min(value = 0, message = "score.min")
    private Integer score;
    private String comment;
}
