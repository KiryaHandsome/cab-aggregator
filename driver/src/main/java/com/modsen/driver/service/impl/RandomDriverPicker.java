package com.modsen.driver.service.impl;

import com.modsen.driver.dto.response.DriverResponse;
import com.modsen.driver.exception.NoAvailableDriversException;
import com.modsen.driver.mapper.DriverMapper;
import com.modsen.driver.model.Driver;
import com.modsen.driver.model.Status;
import com.modsen.driver.repository.DriverRepository;
import com.modsen.driver.service.DriverPicker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RandomDriverPicker implements DriverPicker {

    private final DriverMapper driverMapper;
    private final DriverRepository driverRepository;

    @Override
    @Retryable(
            maxAttempts = 100,
            backoff = @Backoff(delay = 3),
            retryFor = NoAvailableDriversException.class
    )
    public DriverResponse pickAvailable() {
        Driver driver = driverRepository.findAll(Pageable.ofSize(1))
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoAvailableDriversException("Driver not found"));
        driver.setStatus(Status.BUSY);
        driverRepository.save(driver);
        return driverMapper.toResponse(driver);
    }
}
