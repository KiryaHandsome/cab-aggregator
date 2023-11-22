package com.modsen.driver.service.impl;

import com.modsen.driver.dto.response.RatingResponse;
import com.modsen.driver.exception.DriverNotFoundException;
import com.modsen.driver.exception.RatingNotFoundException;
import com.modsen.driver.mapper.RatingMapper;
import com.modsen.driver.model.Rating;
import com.modsen.driver.repository.RatingRepository;
import com.modsen.driver.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;

@Service
@Transactional
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingMapper mapper;
    private final RatingRepository ratingRepository;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Override
    @Transactional(readOnly = true)
    public RatingResponse getRating(Integer driverId) {
        return ratingRepository.findByDriverId(driverId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RatingNotFoundException(
                        "Rating with such driverId not found, driverId=" + driverId
                ));
    }

    @Override
    @Retryable
    public RatingResponse addScore(Integer driverId, Integer newScore) {
        Rating rating = ratingRepository.findByDriverId(driverId)
                .orElseThrow(() -> new RatingNotFoundException(
                        "Rating with such driverId not found, driverId=" + driverId
                ));
        Float averageRating = calculateNewAvgRating(rating, newScore);
        rating.setAverageRating(roundTo2Digits(averageRating));
        rating.setTotalRatings(rating.getTotalRatings() + 1);
        ratingRepository.save(rating);
        return mapper.toResponse(rating);
    }

    protected Float calculateNewAvgRating(Rating rating, Integer newScore) {
        return (rating.getAverageRating() * rating.getTotalRatings() + newScore) / (rating.getTotalRatings() + 1);
    }

    private Float roundTo2Digits(Float value) {
        String formattedValue = decimalFormat.format(value);
        return Float.parseFloat(formattedValue);
    }
}