package com.modsen.driver.mapper;

import com.modsen.driver.util.TestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class RatingMapperTest {

    private static RatingMapper ratingMapper;

    @BeforeAll
    static void setUp() {
        ratingMapper = Mappers.getMapper(RatingMapper.class);
    }

    @Test
    void toResponse_shouldReturnExpectedResponse() {
        Rating rating = TestData.defaultRating();
        RatingResponse expected = new RatingResponse(TestData.AVERAGE_RATING, TestData.TOTAL_RATINGS);
        RatingResponse actual = ratingMapper.toResponse(rating);

        assertThat(actual).isEqualTo(expected);
    }
}