package com.modsen.ride.service.impl;

import com.modsen.ride.dto.RideRequest;
import com.modsen.ride.dto.RideResponse;
import com.modsen.ride.dto.WaitingRideResponse;
import com.modsen.ride.mapper.RideMapper;
import com.modsen.ride.model.WaitingRide;
import com.modsen.ride.repository.RideRepository;
import com.modsen.ride.repository.WaitingRideRepository;
import com.modsen.ride.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideMapper mapper;
    private final RideRepository rideRepository;
    private final WaitingRideRepository waitingRideRepository;

    @Override
    public void bookRide(RideRequest rideRequest) {
        WaitingRide waitingRide = mapper.toWaitingRide(rideRequest);
        waitingRideRepository.save(waitingRide);
    }

    @Override
    public Page<RideResponse> findByPassengerId(Integer passengerId, Pageable pageable) {
        return rideRepository.findByPassengerId(passengerId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<RideResponse> findByDriverId(Integer driverId, Pageable pageable) {
        return rideRepository.findByDriverId(driverId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<WaitingRideResponse> findWaitingRides(Pageable pageable) {
        return waitingRideRepository.findAll(pageable)
                .map(mapper::toWaitingResponse);
    }
}
