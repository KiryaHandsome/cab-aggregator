package com.modsen.passenger.mapper;

import com.modsen.passenger.dto.PassengerCreate;
import com.modsen.passenger.dto.PassengerResponse;
import com.modsen.passenger.model.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PassengerMapper {

    PassengerResponse toResponse(Passenger passenger);

    Passenger toModel(PassengerCreate dto);
}
