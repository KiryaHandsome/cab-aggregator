package com.modsen.driver.service;

import com.modsen.driver.dto.response.RatingResponse;

public interface RatingService {

    RatingResponse getRating(Integer passengerId);

    RatingResponse addScore(Integer passengerId, Integer newScore);
}
