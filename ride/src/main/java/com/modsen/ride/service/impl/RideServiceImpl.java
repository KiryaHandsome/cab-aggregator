package com.modsen.ride.service.impl;

import com.modsen.ride.dto.RideEvent;
import com.modsen.ride.dto.RideRequest;
import com.modsen.ride.dto.RideResponse;
import com.modsen.ride.dto.WaitingRideResponse;
import com.modsen.ride.mapper.EventMapper;
import com.modsen.ride.mapper.RideMapper;
import com.modsen.ride.model.Ride;
import com.modsen.ride.model.WaitingRide;
import com.modsen.ride.repository.RideRepository;
import com.modsen.ride.repository.WaitingRideRepository;
import com.modsen.ride.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideMapper rideMapper;
    private final EventMapper eventMapper;
    private final RideRepository rideRepository;
    private final WaitingRideRepository waitingRideRepository;

    @Override
    public void bookRide(RideRequest rideRequest) {
        WaitingRide waitingRide = rideMapper.toWaitingRide(rideRequest);
        waitingRideRepository.save(waitingRide);
    }

    @Override
    public Page<RideResponse> findByPassengerId(Integer passengerId, Pageable pageable) {
        return rideRepository.findByPassengerId(passengerId, pageable)
                .map(rideMapper::toResponse);
    }

    @Override
    public Page<RideResponse> findByDriverId(Integer driverId, Pageable pageable) {
        return rideRepository.findByDriverId(driverId, pageable)
                .map(rideMapper::toResponse);
    }

    @Override
    public Page<WaitingRideResponse> findWaitingRides(Pageable pageable) {
        return waitingRideRepository.findAll(pageable)
                .map(rideMapper::toWaitingResponse);
    }

    @Override
    public void startRide(RideEvent event) {
        waitingRideRepository.deleteByPassengerId(event.getPassengerId());
        Ride ride = eventMapper.toModel(event);
        ride.setStartTime(LocalDateTime.now());
        rideRepository.save(ride);
    }

    @Override
    public void endRide(RideEvent event) {
        Ride ride = rideRepository
                .findByPassengerIdAndDriverIdAndFinishTimeIsNull(event.getPassengerId(), event.getDriverId())
                .orElseThrow();
        ride.setFinishTime(LocalDateTime.now());
        rideRepository.save(ride);
    }
}
