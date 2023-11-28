package com.modsen.ride.mapper;


import com.modsen.ride.dto.RideDto;
import com.modsen.ride.dto.request.RideRequest;
import com.modsen.ride.dto.response.WaitingRideResponse;
import com.modsen.ride.model.Ride;
import com.modsen.ride.model.WaitingRide;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RideMapper {

    RideDto toDto(Ride ride);

    WaitingRide toWaitingRide(RideRequest request);

    WaitingRideResponse toWaitingResponse(WaitingRide ride);

    Ride toRide(WaitingRide ride);
}
