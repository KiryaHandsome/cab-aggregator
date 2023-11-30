package com.modsen.passenger.mapper;

import com.modsen.passenger.dto.response.RatingResponse;
import com.modsen.passenger.model.Rating;
import com.modsen.passenger.util.TestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class RatingMapperTest {

    private static RatingMapper mapper;

    @BeforeAll
    static void setUp() {
        mapper = Mappers.getMapper(RatingMapper.class);
    }

    @Test
    void check_mapper_notNull() {
        assertThat(mapper).isNotNull();
    }

    @Test
    void check_toResponse_should_return_sameFieldValues() {
        Rating rating = new Rating(TestData.RATING_ID, TestData.TOTAL_RATINGS, TestData.AVERAGE_RATING, null);

        RatingResponse actual = mapper.toResponse(rating);

        assertThat(actual).isNotNull();
        assertThat(actual.getAverageRating()).isEqualTo(TestData.AVERAGE_RATING);
        assertThat(actual.getTotalRatings()).isEqualTo(TestData.TOTAL_RATINGS);
    }

    @Test
    void check_toResponse_should_return_fieldsEqualNull() {
        Rating rating = new Rating(null, null, null, null);

        RatingResponse actual = mapper.toResponse(rating);

        assertThat(actual).isNotNull();
        assertThat(actual.getAverageRating()).isNull();
        assertThat(actual.getTotalRatings()).isNull();
    }
}