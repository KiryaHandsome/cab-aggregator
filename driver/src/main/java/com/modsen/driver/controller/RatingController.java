package com.modsen.driver.controller;

import com.modsen.driver.controller.openapi.RatingControllerOpenApi;
import com.modsen.driver.dto.RatingResponse;
import com.modsen.driver.dto.ScoreRequest;
import com.modsen.driver.service.RatingService;
import jakarta.validation.Valid;
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
public class RatingController implements RatingControllerOpenApi {

    private final RatingService ratingService;

    @GetMapping("/{driverId}")
    public ResponseEntity<RatingResponse> getRating(@PathVariable Integer driverId) {
        RatingResponse response = ratingService.getRating(driverId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{driverId}")
    public ResponseEntity<RatingResponse> addScore(@PathVariable Integer driverId, @Valid @RequestBody ScoreRequest request) {
        RatingResponse response = ratingService.addScore(driverId, request.getScore());
        return ResponseEntity.ok(response);
    }
}
