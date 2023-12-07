package com.modsen.ride.controller;

import com.modsen.ride.controller.openapi.RideControllerOpenApi;
import com.modsen.ride.dto.RideDto;
import com.modsen.ride.dto.RideStart;
import com.modsen.ride.dto.SharedRideResponse;
import com.modsen.ride.dto.request.RideRequest;
import com.modsen.ride.dto.response.WaitingRideResponse;
import com.modsen.ride.service.KafkaChannelGateway;
import com.modsen.ride.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1/rides")
@RequiredArgsConstructor
public class RideController implements RideControllerOpenApi {

    private final RideService rideService;
    private final KafkaChannelGateway kafkaChannelGateway;

    @PostMapping("/book")
    public ResponseEntity<WaitingRideResponse> bookRide(@RequestBody @Valid RideRequest rideRequest) {
        WaitingRideResponse response = rideService.bookRide(rideRequest);
        kafkaChannelGateway.sendToRideOrdered(rideRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-shared-ride")
    public ResponseEntity<SharedRideResponse> checkSharedRide(
            @RequestParam("driver-id") Integer driverId,
            @RequestParam("passenger-id") Integer passengerId
    ) {
        SharedRideResponse response = rideService.haveSharedRide(driverId, passengerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/waiting")
    public ResponseEntity<Page<WaitingRideResponse>> getWaitingRides(Pageable pageable) {
        Page<WaitingRideResponse> response = rideService.findWaitingRides(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/passengers/{passengerId}")
    public ResponseEntity<Page<RideDto>> getPassengerRides(@PathVariable Integer passengerId, Pageable pageable) {
        Page<RideDto> response = rideService.findByPassengerId(passengerId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/drivers/{driverId}")
    public ResponseEntity<Page<RideDto>> getDriverRides(@PathVariable Integer driverId, Pageable pageable) {
        Page<RideDto> response = rideService.findByDriverId(driverId, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/start/{waitingRideId}")
    public ResponseEntity<RideDto> startRide(@PathVariable String waitingRideId, @RequestBody RideStart request) {
        RideDto rideDto = rideService.startRide(waitingRideId, request);
        return ResponseEntity.ok(rideDto);
    }

    @PostMapping("/end/{rideId}")
    public ResponseEntity<RideDto> endRide(@PathVariable String rideId) {
        RideDto rideDto = rideService.endRide(rideId);
        kafkaChannelGateway.sendToRidePayment(rideDto);
        return ResponseEntity.ok(rideDto);
    }
}
