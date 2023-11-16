package com.modsen.passenger.mapper;

import com.modsen.passenger.dto.response.RatingResponse;
import com.modsen.passenger.model.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RatingMapper {

    RatingResponse toResponse(Rating rating);
}
