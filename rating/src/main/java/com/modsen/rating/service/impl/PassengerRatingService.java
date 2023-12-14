package com.modsen.rating.service.impl;

import com.modsen.rating.dto.AvgRatingResponse;
import com.modsen.rating.dto.RatingResponse;
import com.modsen.rating.dto.ScoreRequest;
import com.modsen.rating.exception.RatingNotFoundException;
import com.modsen.rating.model.PassengerRating;
import com.modsen.rating.repository.PassengerRatingRepository;
import com.modsen.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("passenger")
@Transactional
@RequiredArgsConstructor
public class PassengerRatingService implements RatingService {

    private final PassengerRatingRepository passengerRatingRepository;

    @Override
    public void addScore(ScoreRequest request) {
        PassengerRating passengerRating = mapToPassengerRating(request);
        passengerRatingRepository.save(passengerRating);
    }

    @Override
    public AvgRatingResponse getAverageRating(int passengerId) {
        Float averageRating = passengerRatingRepository.getAverageRatingByPassengerId(passengerId)
                .orElseThrow(() -> new RatingNotFoundException("exception.passenger.rating_not_found", passengerId));
        return new AvgRatingResponse(averageRating);
    }

    @Override
    public Page<RatingResponse> getRatingsWithCommentsByUserId(int driverId, Pageable pageable) {
        return passengerRatingRepository.findAllByPassengerIdAndCommentIsNotNull(driverId, pageable)
                .map(this::mapToRatingResponse);
    }

    private RatingResponse mapToRatingResponse(PassengerRating driverRating) {
        return RatingResponse.builder()
                .id(driverRating.getId())
                .userId(driverRating.getPassengerId())
                .score(driverRating.getScore())
                .comment(driverRating.getComment())
                .build();
    }

    private PassengerRating mapToPassengerRating(ScoreRequest request) {
        return PassengerRating.builder()
                .passengerId(request.getUserId())
                .score(request.getScore())
                .comment(request.getComment())
                .build();
    }
}
