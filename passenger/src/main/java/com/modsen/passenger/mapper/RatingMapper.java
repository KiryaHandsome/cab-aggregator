package com.modsen.passenger.mapper;

import com.modsen.passenger.dto.response.RatingResponse;
import com.modsen.passenger.model.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RatingMapper {

    RatingResponse toResponse(Rating rating);
}
