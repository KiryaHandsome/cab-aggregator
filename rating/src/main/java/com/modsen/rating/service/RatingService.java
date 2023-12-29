package com.modsen.rating.service;

import com.modsen.rating.dto.AvgRatingResponse;
import com.modsen.rating.dto.RatingResponse;
import com.modsen.rating.dto.ScoreRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RatingService {

    void addScore(Integer userId, ScoreRequest request);

    AvgRatingResponse getAverageRating(int userId);

    Page<RatingResponse> getRatingsWithCommentsByUserId(int userId, Pageable pageable);
}
