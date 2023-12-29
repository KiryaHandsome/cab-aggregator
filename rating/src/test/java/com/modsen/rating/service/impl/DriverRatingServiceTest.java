package com.modsen.rating.service.impl;

import com.modsen.rating.dto.AvgRatingResponse;
import com.modsen.rating.dto.RatingResponse;
import com.modsen.rating.dto.ScoreRequest;
import com.modsen.rating.exception.RatingNotFoundException;
import com.modsen.rating.model.DriverRating;
import com.modsen.rating.repository.DriverRatingRepository;
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
class DriverRatingServiceTest {

    @Mock
    private DriverRatingRepository driverRatingRepository;

    @InjectMocks
    private DriverRatingService driverRatingService;

    @Test
    void addScore_shouldCallRepositorySaveWithExpectedArg() {
        ScoreRequest request = new ScoreRequest(TestData.SCORE, TestData.COMMENT);
        DriverRating expectedArg = DriverRating.builder()
                .driverId(TestData.DRIVER_ID)
                .score(TestData.SCORE)
                .comment(TestData.COMMENT)
                .build();

        driverRatingService.addScore(TestData.DRIVER_ID, request);

        verify(driverRatingRepository).save(eq(expectedArg));
    }

    @Test
    void getAverageRating_shouldReturnExpectedRating() {
        AvgRatingResponse expectedResponse = new AvgRatingResponse(TestData.AVG_RATING);

        doReturn(Optional.of(TestData.AVG_RATING))
                .when(driverRatingRepository)
                .getAverageRatingByDriverId(TestData.DRIVER_ID);

        AvgRatingResponse averageRating = driverRatingService.getAverageRating(TestData.DRIVER_ID);

        assertThat(averageRating).isEqualTo(expectedResponse);
    }

    @Test
    void getAverageRating_shouldThrowRatingNotFoundException() {
        doReturn(Optional.empty())
                .when(driverRatingRepository)
                .getAverageRatingByDriverId(TestData.DRIVER_ID);

        assertThrows(RatingNotFoundException.class,
                () -> driverRatingService.getAverageRating(TestData.DRIVER_ID));

        verify(driverRatingRepository).getAverageRatingByDriverId(eq(TestData.DRIVER_ID));
    }

    @Test
    void getRatingsWithCommentsByUserId_shouldReturnExpectedPage() {
        Pageable pageable = Pageable.ofSize(10);
        DriverRating rating = new DriverRating(TestData.ID, TestData.DRIVER_ID, TestData.SCORE, TestData.COMMENT);
        RatingResponse expected = new RatingResponse(TestData.ID, TestData.DRIVER_ID, TestData.SCORE, TestData.COMMENT);

        doReturn(new PageImpl<>(List.of(rating)))
                .when(driverRatingRepository)
                .findAllByDriverIdAndCommentIsNotNull(TestData.DRIVER_ID, pageable);

        Page<RatingResponse> actual = driverRatingService.getRatingsWithCommentsByUserId(TestData.DRIVER_ID, pageable);

        assertThat(actual).hasSize(1);
        assertThat(actual).allMatch(p -> p.equals(expected));
    }
}