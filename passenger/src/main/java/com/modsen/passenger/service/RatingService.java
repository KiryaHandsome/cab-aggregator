package com.modsen.passenger.service;

public interface RatingService {

    Float getRating(Integer passengerId);

    Float addScore(Float newScore);
}
