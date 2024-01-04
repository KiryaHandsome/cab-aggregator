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

@RestController
@RequestMapping("/api/v1/ratings/drivers")
public class DriverRatingController {

    private final RatingService driverRatingService;

    public DriverRatingController(@Qualifier("driver") RatingService driverRatingService) {
        this.driverRatingService = driverRatingService;
    }

    @GetMapping("/{driverId}/avg")
    @ResponseStatus(HttpStatus.OK)
    public AvgRatingResponse getAverageDriverRating(@PathVariable Integer driverId) {
        return driverRatingService.getAverageRating(driverId);
    }

    @PostMapping("/{driverId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addScoreToDriver(@PathVariable Integer driverId, @Valid @RequestBody ScoreRequest scoreRequest) {
        driverRatingService.addScore(driverId, scoreRequest);
    }

    @GetMapping("/{driverId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<RatingResponse> getDriverRatingsWithComments(@PathVariable Integer driverId, Pageable pageable) {
        return driverRatingService.getRatingsWithCommentsByUserId(driverId, pageable);
    }
}
