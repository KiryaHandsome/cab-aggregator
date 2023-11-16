package com.modsen.driver.mapper;

import com.modsen.driver.dto.request.DriverCreate;
import com.modsen.driver.dto.response.DriverResponse;
import com.modsen.driver.model.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DriverMapper {

    DriverResponse toResponse(Driver driver);

    Driver toModel(DriverCreate request);
}
