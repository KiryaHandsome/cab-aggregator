package com.modsen.passenger.mapper;

import com.modsen.passenger.dto.request.PassengerCreate;
import com.modsen.passenger.dto.request.PassengerUpdate;
import com.modsen.passenger.dto.response.PassengerResponse;
import com.modsen.passenger.model.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PassengerMapper {

    PassengerResponse toResponse(Passenger passenger);

    Passenger toModel(PassengerCreate dto);

    @Mapping(source = "name", target = "name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "surname", target = "surname", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "email", target = "email", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "phoneNumber", target = "phoneNumber", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateIfNotNull(PassengerUpdate source, @MappingTarget Passenger target);
}
