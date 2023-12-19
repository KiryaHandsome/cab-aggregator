package com.modsen.ride.service.impl;

import com.modsen.ride.dto.DriverStatus;
import com.modsen.ride.dto.RideDto;
import com.modsen.ride.dto.response.DriverResponse;
import com.modsen.ride.dto.response.WaitingRideResponse;
import com.modsen.ride.mapper.RideMapper;
import com.modsen.ride.model.Ride;
import com.modsen.ride.repository.RideRepository;
import com.modsen.ride.repository.WaitingRideRepository;
import com.modsen.ride.service.CostCalculator;
import com.modsen.ride.service.DriverClient;
import com.modsen.ride.util.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RideServiceImplTest {

    @Mock
    private RideMapper rideMapper;

    @Mock
    private RideRepository rideRepository;

    @Mock
    private WaitingRideRepository waitingRideRepository;

    @Mock
    private CostCalculator costCalculator;

    @Mock
    private DriverClient driverClient;

    @InjectMocks
    private RideServiceImpl rideService;

    @Test
    void bookRide_shouldSaveRide() {
        doReturn(TestData.defaultWaitingRide())
                .when(rideMapper)
                .toWaitingRide(TestData.rideRequest());

        rideService.bookRide(TestData.rideRequest());

        verify(rideMapper).toWaitingRide(TestData.rideRequest());
        verify(waitingRideRepository).save(eq(TestData.defaultWaitingRide()));
    }

    @Test
    void findByPassengerId_shouldReturnPassengersRide() {
        Pageable pageable = Pageable.ofSize(2);
        List<Ride> content = List.of(
                TestData.defaultRide(),
                TestData.defaultRide()
        );

        doReturn(new PageImpl<>(content))
                .when(rideRepository)
                .findByPassengerId(TestData.PASSENGER_ID, pageable);
        doReturn(TestData.defaultRideDto())
                .when(rideMapper)
                .toDto(TestData.defaultRide());

        Page<RideDto> rides = rideService.findByPassengerId(TestData.PASSENGER_ID, pageable);

        assertThat(rides).hasSize(2);
        assertThat(rides.getContent()).contains(TestData.defaultRideDto());

        verify(rideRepository).findByPassengerId(eq(TestData.PASSENGER_ID), eq(pageable));
        verify(rideMapper, times(2)).toDto(eq(TestData.defaultRide()));
    }

    @Test
    void findByDriverId_shouldReturnDriversRides() {
        Pageable pageable = Pageable.ofSize(2);
        List<Ride> content = List.of(
                TestData.defaultRide(),
                TestData.defaultRide()
        );

        doReturn(new PageImpl<>(content))
                .when(rideRepository)
                .findByDriverId(TestData.DRIVER_ID, pageable);
        doReturn(TestData.defaultRideDto())
                .when(rideMapper)
                .toDto(TestData.defaultRide());

        Page<RideDto> rides = rideService.findByDriverId(TestData.DRIVER_ID, pageable);

        assertThat(rides).hasSize(2);
        assertThat(rides.getContent()).contains(TestData.defaultRideDto());

        verify(rideRepository).findByDriverId(eq(TestData.DRIVER_ID), eq(pageable));
        verify(rideMapper, times(2)).toDto(eq(TestData.defaultRide()));
    }

    @Test
    void findWaitingRides_shouldReturnExpectedRides() {
        Pageable pageable = Pageable.ofSize(2);
        doReturn(new PageImpl<>(
                List.of(
                        TestData.defaultWaitingRide(),
                        TestData.defaultWaitingRide()
                )
        ))
                .when(waitingRideRepository)
                .findAll(pageable);
        doReturn(TestData.defaultWaitingRideResponse())
                .when(rideMapper)
                .toWaitingResponse(TestData.defaultWaitingRide());

        Page<WaitingRideResponse> actual = rideService.findWaitingRides(pageable);

        assertThat(actual).hasSize(2);
        assertThat(actual).contains(TestData.defaultWaitingRideResponse());

        verify(waitingRideRepository).findAll(eq(pageable));
        verify(rideMapper, times(2)).toWaitingResponse(TestData.defaultWaitingRide());
    }

    @Test
    void startRide_shouldCallDeleteFromWaitingRidesAndSaveToRideCollection() {
        doReturn(Optional.of(TestData.defaultWaitingRide()))
                .when(waitingRideRepository)
                .findById(TestData.WAITING_RIDE_ID);
        doReturn(TestData.defaultRide())
                .when(rideMapper)
                .toRide(TestData.defaultWaitingRide());
        doReturn(TestData.COST)
                .when(costCalculator)
                .calculate(TestData.FROM, TestData.TO);
        doReturn(new DriverResponse(null, null, null, null, null, DriverStatus.AVAILABLE))
                .when(driverClient)
                .getDriverById(TestData.DRIVER_ID);

        rideService.startRide(TestData.WAITING_RIDE_ID, TestData.rideStart().getDriverId());

        verify(rideMapper).toRide(eq(TestData.defaultWaitingRide()));
        verify(costCalculator).calculate(eq(TestData.FROM), eq(TestData.TO));
        verify(rideRepository).save(any());
        verify(waitingRideRepository).deleteById(eq(TestData.WAITING_RIDE_ID));
    }

    @Test
    void endRide_shouldThrowRuntimeException() {
        doReturn(Optional.empty())
                .when(rideRepository)
                .findById(TestData.RIDE_ID);

        assertThrows(RuntimeException.class,
                () -> rideService.endRide(TestData.RIDE_ID));

        verify(rideRepository)
                .findById(eq(TestData.RIDE_ID));
    }

    @Test
    void endRide_shouldSaveUpdatedRide() {
        doReturn(Optional.of(TestData.defaultRide()))
                .when(rideRepository)
                .findById(TestData.RIDE_ID);

        rideService.endRide(TestData.RIDE_ID);

        verify(rideRepository).findById(eq(TestData.RIDE_ID));
        verify(rideRepository).save(any());
    }
}