package com.modsen.driver.service.impl;

import com.modsen.driver.dto.response.RatingResponse;
import com.modsen.driver.exception.RatingNotFoundException;
import com.modsen.driver.mapper.RatingMapper;
import com.modsen.driver.model.Rating;
import com.modsen.driver.repository.RatingRepository;
import com.modsen.driver.util.TestData;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @Mock
    private RatingMapper ratingMapper;

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingServiceImpl ratingService;


    @Test
    void getRating_shouldThrowNotFoundException() {
        doReturn(Optional.empty())
                .when(ratingRepository)
                .findByDriverId(TestData.DRIVER_ID);

        assertThrows(RatingNotFoundException.class,
                () -> ratingService.getRating(TestData.DRIVER_ID));

        verify(ratingRepository).findByDriverId(eq(TestData.DRIVER_ID));
    }

    @Test
    void getRating_shouldReturnExpectedResponse() {
        Rating rating = TestData.defaultRating();
        RatingResponse expected = TestData.defaultRatingResponse();

        doReturn(Optional.of(rating))
                .when(ratingRepository)
                .findByDriverId(TestData.DRIVER_ID);
        doReturn(expected)
                .when(ratingMapper)
                .toResponse(rating);

        RatingResponse actual = ratingService.getRating(TestData.DRIVER_ID);

        assertThat(actual).isEqualTo(expected);

        verify(ratingRepository).findByDriverId(eq(TestData.DRIVER_ID));
        verify(ratingMapper).toResponse(eq(rating));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void addScore_shouldReturnExpected(int newScore) {
        Rating rating = TestData.defaultRating();
        float newRating = (rating.getAverageRating() * rating.getTotalRatings() + newScore) / (rating.getTotalRatings() + 1);
        Rating updatedRating = new Rating(
                TestData.RATING_ID,
                TestData.TOTAL_RATINGS + 1,
                Math.round(newRating * 100.0f) / 100.0f,
                TestData.defaultDriver()
        );
        RatingResponse expected = new RatingResponse(TestData.AVERAGE_RATING, TestData.TOTAL_RATINGS);

        doReturn(Optional.of(rating))
                .when(ratingRepository)
                .findByDriverId(TestData.DRIVER_ID);
        doReturn(expected)
                .when(ratingMapper)
                .toResponse(updatedRating);

        RatingResponse actual = ratingService.addScore(TestData.DRIVER_ID, newScore);

        assertThat(actual).isEqualTo(expected);

        verify(ratingRepository).findByDriverId(eq(TestData.DRIVER_ID));
        verify(ratingMapper).toResponse(eq(rating));
    }

    @Test
    void addScore_shouldThrowRatingNotFoundException() {
        doReturn(Optional.empty())
                .when(ratingRepository)
                .findByDriverId(TestData.DRIVER_ID);

        assertThrows(RatingNotFoundException.class,
                () -> ratingService.addScore(TestData.DRIVER_ID, 5));

        verify(ratingRepository).findByDriverId(eq(TestData.DRIVER_ID));
    }


    @Nested
    class CalculateNewAvgRating {

        private static Stream<RatingParams> ratingParams() {
            return Stream.of(
                    new RatingParams(1, 4.4f),
                    new RatingParams(5, 1.2f),
                    new RatingParams(10, 3.5f)
            );
        }

        @Test
        void withNullParams_shouldReturnCloseResult() {
            Rating rating = new Rating(null, 0, 0f, null);

            Float actual = ratingService.calculateNewAvgRating(rating, 5);

            assertThat(actual).isNotNull();
            assertThat(actual).isCloseTo(5f, Offset.offset(0.001f));
        }

        @ParameterizedTest
        @MethodSource("ratingParams")
        void withNotNullParams_shouldReturnCloseResult(RatingParams inputParams) {
            int newScore = 5;
            Rating rating = new Rating(null, inputParams.totalRatings(), inputParams.avgRating(), null);
            Float expected = (rating.getAverageRating() * rating.getTotalRatings() + newScore) / (rating.getTotalRatings() + 1);

            Float actual = ratingService.calculateNewAvgRating(rating, newScore);

            assertThat(actual).isNotNull();
            assertThat(actual).isCloseTo(expected, Offset.offset(0.001f));
        }

        record RatingParams(
                Integer totalRatings,
                Float avgRating
        ) {

        }
    }

}