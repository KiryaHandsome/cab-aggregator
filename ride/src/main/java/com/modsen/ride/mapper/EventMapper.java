package com.modsen.ride.mapper;

import com.modsen.ride.dto.RideEvent;
import com.modsen.ride.model.Ride;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EventMapper {

    Ride toModel(RideEvent event);
}
