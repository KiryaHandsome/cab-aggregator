package com.modsen.driver.service.impl;

import com.modsen.driver.dto.event.RideOrdered;
import com.modsen.driver.model.Status;
import com.modsen.driver.repository.DriverRepository;
import com.modsen.driver.service.DriverNotifier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FakeDriverNotifier implements DriverNotifier {

    private final DriverRepository driverRepository;

    @Override
    public void notifyAvailableDrivers(RideOrdered rideOrdered) {
        driverRepository.findAll(Pageable.ofSize(1))
                .stream()
                .filter(d -> Status.AVAILABLE.equals(d.getStatus()))
                .forEach(d -> { /* notifying action*/ });
    }
}
