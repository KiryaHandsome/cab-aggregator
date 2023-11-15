package com.modsen.ride.controller;

import com.modsen.ride.controller.openapi.RideControllerOpenApi;
import com.modsen.ride.dto.RideRequest;
import com.modsen.ride.dto.RideResponse;
import com.modsen.ride.service.RideService;
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

    @PostMapping
    public ResponseEntity<?> bookRide(@RequestBody RideRequest rideRequest) {
        rideService.bookRide(rideRequest);
        return ResponseEntity.ok()
                .build();
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
}
