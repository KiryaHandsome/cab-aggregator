package com.modsen.passenger.mapper;

import com.modsen.passenger.dto.request.PassengerCreate;
import com.modsen.passenger.dto.response.PassengerResponse;
import com.modsen.passenger.model.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PassengerMapper {

    PassengerResponse toResponse(Passenger passenger);

    Passenger toModel(PassengerCreate dto);
}
