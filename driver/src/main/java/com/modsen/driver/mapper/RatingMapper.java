package com.modsen.driver.mapper;

import com.modsen.driver.dto.response.RatingResponse;
import com.modsen.driver.model.Rating;
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
