package com.modsen.ride.service.impl;

import com.modsen.ride.dto.RideRequest;
import com.modsen.ride.dto.RideResponse;
import com.modsen.ride.dto.RideStart;
import com.modsen.ride.dto.WaitingRideResponse;
import com.modsen.ride.exception.RideAlreadyEndedException;
import com.modsen.ride.exception.RideNotFoundException;
import com.modsen.ride.exception.WaitingRideNotFoundException;
import com.modsen.ride.mapper.RideMapper;
import com.modsen.ride.model.Ride;
import com.modsen.ride.model.WaitingRide;
import com.modsen.ride.repository.RideRepository;
import com.modsen.ride.repository.WaitingRideRepository;
import com.modsen.ride.service.CostCalculator;
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
    private final RideRepository rideRepository;
    private final CostCalculator costCalculator;
    private final WaitingRideRepository waitingRideRepository;

    @Override
    public WaitingRideResponse bookRide(RideRequest rideRequest) {
        WaitingRide waitingRide = rideMapper.toWaitingRide(rideRequest);
        waitingRideRepository.save(waitingRide);
        return rideMapper.toWaitingResponse(waitingRide);
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
    public RideResponse startRide(String waitingRideId, RideStart event) {
        WaitingRide waitingRide = waitingRideRepository.findById(waitingRideId)
                .orElseThrow(() -> new WaitingRideNotFoundException("exception.waiting_ride_not_found", waitingRideId));
        Ride ride = rideMapper.toRide(waitingRide);
        ride.setDriverId(event.getDriverId());
        ride.setStartTime(LocalDateTime.now());
        ride.setCost(costCalculator.calculate(ride.getFrom(), ride.getTo()));
        rideRepository.save(ride);
        waitingRideRepository.deleteById(waitingRideId);
        return rideMapper.toResponse(ride);
    }

    @Override
    public RideResponse endRide(String rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException("exception.ride_not_found", rideId));
        if (ride.getFinishTime() != null) {
            throw new RideAlreadyEndedException("exception.ride_already_ended", rideId);
        }
        ride.setFinishTime(LocalDateTime.now());
        rideRepository.save(ride);
        return rideMapper.toResponse(ride);
    }
}
