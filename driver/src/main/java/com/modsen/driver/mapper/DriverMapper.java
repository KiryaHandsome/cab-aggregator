package com.modsen.driver.mapper;

import com.modsen.driver.dto.DriverCreate;
import com.modsen.driver.dto.DriverResponse;
import com.modsen.driver.model.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DriverMapper {

    DriverResponse toResponse(Driver driver);

    Driver toModel(DriverCreate request);
}
