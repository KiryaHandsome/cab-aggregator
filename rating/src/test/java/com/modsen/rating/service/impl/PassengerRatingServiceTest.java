package com.modsen.rating.service.impl;

import com.modsen.rating.dto.AvgRatingResponse;
import com.modsen.rating.dto.RatingResponse;
import com.modsen.rating.dto.ScoreRequest;
import com.modsen.rating.exception.RatingNotFoundException;
import com.modsen.rating.model.PassengerRating;
import com.modsen.rating.repository.PassengerRatingRepository;
import com.modsen.rating.util.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PassengerRatingServiceTest {

    @Mock
    private PassengerRatingRepository passengerRatingRepository;

    @InjectMocks
    private PassengerRatingService passengerRatingService;

    @Test
    void addScore_shouldCallRepositorySaveWithExpectedArg() {
        ScoreRequest request = new ScoreRequest(TestData.DRIVER_ID, TestData.SCORE, TestData.COMMENT);
        PassengerRating expectedArg = PassengerRating.builder()
                .passengerId(TestData.DRIVER_ID)
                .score(TestData.SCORE)
                .comment(TestData.COMMENT)
                .build();

        passengerRatingService.addScore(request);

        verify(passengerRatingRepository).save(eq(expectedArg));
    }

    @Test
    void getAverageRating_shouldReturnExpectedRating() {
        AvgRatingResponse expectedResponse = new AvgRatingResponse(TestData.AVG_RATING);

        doReturn(Optional.of(TestData.AVG_RATING))
                .when(passengerRatingRepository)
                .getAverageRatingByPassengerId(TestData.DRIVER_ID);

        AvgRatingResponse averageRating = passengerRatingService.getAverageRating(TestData.DRIVER_ID);

        assertThat(averageRating).isEqualTo(expectedResponse);
    }

    @Test
    void getAverageRating_shouldThrowRatingNotFoundException() {
        doReturn(Optional.empty())
                .when(passengerRatingRepository)
                .getAverageRatingByPassengerId(TestData.DRIVER_ID);

        assertThrows(RatingNotFoundException.class,
                () -> passengerRatingService.getAverageRating(TestData.DRIVER_ID));

        verify(passengerRatingRepository).getAverageRatingByPassengerId(eq(TestData.DRIVER_ID));
    }

    @Test
    void getRatingsWithCommentsByUserId_shouldReturnExpectedPage() {
        Pageable pageable = Pageable.ofSize(10);
        PassengerRating rating = new PassengerRating(TestData.ID, TestData.DRIVER_ID, TestData.SCORE, TestData.COMMENT);
        RatingResponse expected = new RatingResponse(TestData.ID, TestData.DRIVER_ID, TestData.SCORE, TestData.COMMENT);

        doReturn(new PageImpl<>(List.of(rating)))
                .when(passengerRatingRepository)
                .findAllByPassengerIdAndCommentIsNotNull(TestData.DRIVER_ID, pageable);

        Page<RatingResponse> actual = passengerRatingService.getRatingsWithCommentsByUserId(TestData.DRIVER_ID, pageable);

        assertThat(actual).hasSize(1);
        assertThat(actual).allMatch(p -> p.equals(expected));
    }
}