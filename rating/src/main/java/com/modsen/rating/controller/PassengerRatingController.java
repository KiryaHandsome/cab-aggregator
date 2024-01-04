package com.modsen.rating.controller;

import com.modsen.rating.dto.AvgRatingResponse;
import com.modsen.rating.dto.RatingResponse;
import com.modsen.rating.dto.ScoreRequest;
import com.modsen.rating.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/ratings/passengers")
@RestController
public class PassengerRatingController {

    private final RatingService passengerRatingService;

    public PassengerRatingController(@Qualifier("passenger") RatingService passengerRatingService) {
        this.passengerRatingService = passengerRatingService;
    }

    @GetMapping("/{passengerId}/avg")
    @ResponseStatus(HttpStatus.OK)
    public AvgRatingResponse getAveragePassengerRating(@PathVariable Integer passengerId) {
        return passengerRatingService.getAverageRating(passengerId);
    }

    @PostMapping("/{passengerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addScoreToPassenger(@PathVariable Integer passengerId, @Valid @RequestBody ScoreRequest scoreRequest) {
        passengerRatingService.addScore(passengerId, scoreRequest);
    }

    @GetMapping("/{passengerId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<RatingResponse> addScoreToPassenger(@PathVariable Integer passengerId, Pageable pageable) {
        return passengerRatingService.getRatingsWithCommentsByUserId(passengerId, pageable);
    }
}
