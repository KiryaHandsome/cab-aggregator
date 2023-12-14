package com.modsen.ride.service.impl;


import com.modsen.ride.dto.DriverStatus;
import com.modsen.ride.dto.PaymentEvent;
import com.modsen.ride.dto.RideDto;
import com.modsen.ride.dto.RideStart;
import com.modsen.ride.dto.SharedRideResponse;
import com.modsen.ride.dto.StatusUpdate;
import com.modsen.ride.dto.request.RideRequest;
import com.modsen.ride.dto.response.DriverResponse;
import com.modsen.ride.dto.response.WaitingRideResponse;
import com.modsen.ride.exception.DriverNotAvailableException;
import com.modsen.ride.exception.RideAlreadyEndedException;
import com.modsen.ride.exception.RideNotFoundException;
import com.modsen.ride.exception.WaitingRideNotFoundException;
import com.modsen.ride.mapper.RideMapper;
import com.modsen.ride.model.PaymentStatus;
import com.modsen.ride.model.Ride;
import com.modsen.ride.model.WaitingRide;
import com.modsen.ride.repository.RideRepository;
import com.modsen.ride.repository.WaitingRideRepository;
import com.modsen.ride.service.CostCalculator;
import com.modsen.ride.service.DriverClient;
import com.modsen.ride.service.RideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideMapper rideMapper;
    private final DriverClient driverClient;
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
    public Page<RideDto> findByPassengerId(Integer passengerId, Pageable pageable) {
        return rideRepository.findByPassengerId(passengerId, pageable)
                .map(rideMapper::toDto);
    }

    @Override
    public Page<RideDto> findByDriverId(Integer driverId, Pageable pageable) {
        return rideRepository.findByDriverId(driverId, pageable)
                .map(rideMapper::toDto);
    }

    @Override
    public Page<WaitingRideResponse> findWaitingRides(Pageable pageable) {
        return waitingRideRepository.findAll(pageable)
                .map(rideMapper::toWaitingResponse);
    }

    @Override
    public RideDto startRide(String waitingRideId, RideStart event) {
        WaitingRide waitingRide = waitingRideRepository.findById(waitingRideId)
                .orElseThrow(() -> new WaitingRideNotFoundException("exception.waiting_ride_not_found", waitingRideId));
        DriverResponse driverResponse = driverClient.getDriverById(event.getDriverId());
        checkDriverAvailable(driverResponse, event.getDriverId());
        driverClient.updateDriver(event.getDriverId(), new StatusUpdate(DriverStatus.BUSY));
        return saveNewRide(waitingRide, event.getDriverId());
    }

    private void checkDriverAvailable(DriverResponse response, Integer driverId) {
        boolean driverAvailable = DriverStatus.AVAILABLE.equals(response.getStatus());
        if (!driverAvailable) {
            log.warn("Driver service returned bad driver status={}", response.getStatus());
            throw new DriverNotAvailableException("exception.driver_not_available",
                    driverId, response.getStatus());
        }
    }

    private RideDto saveNewRide(WaitingRide waitingRide, Integer driverId) {
        Ride ride = rideMapper.toRide(waitingRide);
        setupRide(ride, driverId);
        rideRepository.save(ride);
        waitingRideRepository.deleteById(waitingRide.getId());
        return rideMapper.toDto(ride);
    }


    private void setupRide(Ride ride, Integer driverId) {
        ride.setDriverId(driverId);
        ride.setStartTime(LocalDateTime.now());
        Float cost = costCalculator.calculate(ride.getFrom(), ride.getTo());
        ride.setCost(cost);
        ride.setPaymentStatus(PaymentStatus.WAITING);
    }

    @Override
    public RideDto endRide(String rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException("exception.ride_not_found", rideId));
        if (ride.getFinishTime() != null) {
            throw new RideAlreadyEndedException("exception.ride_already_ended", rideId);
        }
        driverClient.updateDriver(ride.getDriverId(), new StatusUpdate(DriverStatus.AVAILABLE));
        ride.setFinishTime(LocalDateTime.now());
        rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    @Override
    public void handlePaymentResult(PaymentEvent paymentEvent) {
        log.debug("HandlePaymentResult: {}", paymentEvent);
        Ride ride = rideRepository.findById(paymentEvent.getRideId())
                .orElseThrow(() -> new RideNotFoundException("exception.ride_not_found", paymentEvent.getRideId()));
        ride.setPaymentStatus(paymentEvent.getStatus());
        rideRepository.save(ride);
    }

    @Override
    public SharedRideResponse haveSharedRide(Integer driverId, Integer passengerId) {
        boolean haveSharedRide = rideRepository
                .findFirstByDriverIdAndPassengerId(driverId, passengerId)
                .isPresent();
        return new SharedRideResponse(haveSharedRide);
    }
}
