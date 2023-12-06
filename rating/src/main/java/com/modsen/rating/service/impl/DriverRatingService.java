package com.modsen.rating.service.impl;

import com.modsen.rating.dto.AvgRatingResponse;
import com.modsen.rating.dto.RatingResponse;
import com.modsen.rating.dto.ScoreRequest;
import com.modsen.rating.exception.RatingNotFoundException;
import com.modsen.rating.model.DriverRating;
import com.modsen.rating.repository.DriverRatingRepository;
import com.modsen.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("driver")
@Transactional
@RequiredArgsConstructor
public class DriverRatingService implements RatingService {

    private final DriverRatingRepository driverRatingRepository;

    @Override
    public void addScore(ScoreRequest request) {
        DriverRating driverRating = mapToDriverRating(request);
        driverRatingRepository.save(driverRating);
    }

    @Override
    public AvgRatingResponse getAverageRating(int driverId) {
        Float averageRating = driverRatingRepository.getAverageRatingByDriverId(driverId)
                .orElseThrow(() -> new RatingNotFoundException("exception.driver.rating_not_found", driverId));
        return new AvgRatingResponse(averageRating);
    }

    @Override
    public Page<RatingResponse> getRatingsWithCommentsByUserId(int driverId, Pageable pageable) {
        return driverRatingRepository.getAllByDriverIdAndCommentIsNotNull(driverId, pageable)
                .map(this::mapToRatingResponse);
    }

    private RatingResponse mapToRatingResponse(DriverRating driverRating) {
        return RatingResponse.builder()
                .id(driverRating.getId())
                .userId(driverRating.getDriverId())
                .score(driverRating.getScore())
                .comment(driverRating.getComment())
                .build();
    }

    private DriverRating mapToDriverRating(ScoreRequest request) {
        return DriverRating.builder()
                .driverId(request.getUserId())
                .score(request.getScore())
                .comment(request.getComment())
                .build();
    }
}
