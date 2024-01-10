package com.modsen.ride.service.client;

import com.modsen.ride.config.DriverClientConfig;
import com.modsen.ride.dto.request.StatusUpdate;
import com.modsen.ride.dto.response.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "${driver.name}", url = "${driver.url}", path = "${driver.path}",
        configuration = DriverClientConfig.class
)
public interface DriverClient {

    @GetMapping("/{id}")
    DriverResponse getDriverById(@PathVariable("id") Integer id);

    @PatchMapping("/{id}")
    DriverResponse updateDriver(@PathVariable("id") Integer id, @RequestBody StatusUpdate statusUpdate);
}
