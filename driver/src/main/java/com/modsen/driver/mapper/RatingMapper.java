package com.modsen.driver.mapper;

import com.modsen.driver.dto.RatingResponse;
import com.modsen.driver.model.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RatingMapper {

    @Mapping(target = "totalRatings", source = "totalRatings")
    @Mapping(target = "averageRating", source = "averageRating")
    RatingResponse toResponse(Rating rating);
}
