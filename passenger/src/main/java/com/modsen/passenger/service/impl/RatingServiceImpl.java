package com.modsen.passenger.service.impl;

import com.modsen.passenger.dto.response.RatingResponse;
import com.modsen.passenger.exception.RatingNotFoundException;
import com.modsen.passenger.mapper.RatingMapper;
import com.modsen.passenger.model.Rating;
import com.modsen.passenger.repository.RatingRepository;
import com.modsen.passenger.service.RatingService;
import lombok.RequiredArgsConstructor;
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
    public RatingResponse getRating(Integer passengerId) {
        return ratingRepository.findByPassengerId(passengerId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RatingNotFoundException(
                        "Rating with such passengerId not found, passengerId=" + passengerId
                ));
    }

    @Override
    public RatingResponse addScore(Integer passengerId, Integer newScore) {
        Rating rating = ratingRepository.findByPassengerId(passengerId)
                .orElseThrow(() -> new RatingNotFoundException(
                        "Rating with such passengerId not found, passengerId=" + passengerId
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
