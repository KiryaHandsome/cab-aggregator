package com.modsen.passenger.service;

import com.modsen.passenger.dto.response.RatingResponse;

public interface RatingService {

    RatingResponse getRating(Integer passengerId);

    RatingResponse addScore(Integer passengerId, Integer newScore);
}
