package com.modsen.passenger.controller;

import com.modsen.passenger.dto.RatingResponse;
import com.modsen.passenger.dto.ScoreRequest;
import com.modsen.passenger.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("/{passengerId}")
    public ResponseEntity<RatingResponse> getRating(@PathVariable Integer passengerId) {
        RatingResponse response = ratingService.getRating(passengerId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{passengerId}")
    public ResponseEntity<RatingResponse> addScore(@PathVariable Integer passengerId, @RequestBody ScoreRequest request) {
        RatingResponse response = ratingService.addScore(passengerId, request.getScore());
        return ResponseEntity.ok(response);
    }
}
