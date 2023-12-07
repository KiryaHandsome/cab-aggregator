package com.modsen.rating.service;

import com.modsen.rating.dto.SharedRideResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("${ride.name}")
public interface RideClient {

    @GetMapping("/api/v1/rides/check-shared-ride")
    SharedRideResponse haveSharedRide(@RequestParam("driver-id") Integer driverId,
                                      @RequestParam("passenger-id") Integer passengerId);
}
