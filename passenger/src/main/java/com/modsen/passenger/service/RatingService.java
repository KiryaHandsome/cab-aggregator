package com.modsen.passenger.service;

import com.modsen.passenger.dto.RatingResponse;

public interface RatingService {

    RatingResponse getRating(Integer passengerId);

    RatingResponse addScore(Integer passengerId, Integer newScore);
}
