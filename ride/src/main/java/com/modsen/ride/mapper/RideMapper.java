package com.modsen.ride.mapper;


import com.modsen.ride.dto.RideRequest;
import com.modsen.ride.dto.RideResponse;
import com.modsen.ride.dto.WaitingRideResponse;
import com.modsen.ride.model.Ride;
import com.modsen.ride.model.WaitingRide;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RideMapper {

    RideResponse toResponse(Ride ride);

    WaitingRide toWaitingRide(RideRequest request);

    WaitingRideResponse toWaitingResponse(WaitingRide ride);
}
