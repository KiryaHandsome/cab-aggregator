package com.modsen.rating.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreRequest {

    @NotNull(message = "score.userId")
    private Integer userId;

    @Max(value = 5, message = "score.max")
    @Min(value = 0, message = "score.min")
    private Integer score;
    private String comment;
}
