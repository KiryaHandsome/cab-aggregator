package com.modsen.driver.mapper;


import com.modsen.driver.dto.event.RideEvent;
import com.modsen.driver.dto.event.RideOrdered;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EventMapper {

    RideEvent toRideEvent(RideOrdered rideOrdered);
}
