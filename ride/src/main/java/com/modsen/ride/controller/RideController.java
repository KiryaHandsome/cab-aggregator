package com.modsen.ride.controller;

import com.modsen.ride.controller.openapi.RideControllerOpenApi;
import com.modsen.ride.dto.RideRequest;
import com.modsen.ride.dto.RideResponse;
import com.modsen.ride.dto.RideStart;
import com.modsen.ride.dto.WaitingRideResponse;
import com.modsen.ride.service.RideService;
import com.modsen.ride.service.impl.KafkaProducer;
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

@Controller
@RequestMapping("/api/v1/rides")
@RequiredArgsConstructor
public class RideController implements RideControllerOpenApi {

    private final RideService rideService;
    private final KafkaProducer kafkaProducer;

    @PostMapping("/book")
    public ResponseEntity<?> bookRide(@RequestBody @Valid RideRequest rideRequest) {
        WaitingRideResponse response = rideService.bookRide(rideRequest);
        kafkaProducer.sendMessage("ride-ordered", rideRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/waiting")
    public ResponseEntity<Page<WaitingRideResponse>> getWaitingRides(Pageable pageable) {
        Page<WaitingRideResponse> response = rideService.findWaitingRides(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/passengers/{passengerId}")
    public ResponseEntity<Page<RideResponse>> getPassengerRides(@PathVariable Integer passengerId, Pageable pageable) {
        Page<RideResponse> response = rideService.findByPassengerId(passengerId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/drivers/{driverId}")
    public ResponseEntity<Page<RideResponse>> getDriverRides(@PathVariable Integer driverId, Pageable pageable) {
        Page<RideResponse> response = rideService.findByDriverId(driverId, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/start/{waitingRideId}")
    public ResponseEntity<RideResponse> startRide(@PathVariable String waitingRideId, @RequestBody RideStart request) {
        RideResponse rideResponse = rideService.startRide(waitingRideId, request);
        return ResponseEntity.ok(rideResponse);
    }

    @PostMapping("/end/{rideId}")
    public ResponseEntity<RideResponse> endRide(@PathVariable String rideId) {
        RideResponse rideResponse = rideService.endRide(rideId);
        return ResponseEntity.ok(rideResponse);
    }
}
