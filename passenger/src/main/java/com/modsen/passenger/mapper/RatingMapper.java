package com.modsen.passenger.mapper;

import com.modsen.passenger.dto.RatingResponse;
import com.modsen.passenger.model.Rating;
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
