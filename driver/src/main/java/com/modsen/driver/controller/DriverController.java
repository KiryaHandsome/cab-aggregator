package com.modsen.driver.controller;

import com.modsen.driver.controller.openapi.DriverControllerOpenApi;
import com.modsen.driver.dto.request.DriverCreate;
import com.modsen.driver.dto.request.DriverUpdate;
import com.modsen.driver.dto.response.DriverResponse;
import com.modsen.driver.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

@Controller
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController implements DriverControllerOpenApi {

    private final DriverService driverService;

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getDriver(@PathVariable Integer id) {
        DriverResponse response = driverService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<DriverResponse>> getAllDrivers(Pageable pageable) {
        Page<DriverResponse> response = driverService.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DriverResponse> updateDriver(
            @PathVariable Integer id,
            @Valid @RequestBody DriverUpdate request
    ) {
        DriverResponse response = driverService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<DriverResponse> createDriver(@Valid @RequestBody DriverCreate request) {
        DriverResponse response = driverService.create(request);
        return ResponseEntity
                .created(URI.create("/api/v1/drivers/" + response.getId()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDriver(@PathVariable Integer id) {
        driverService.deleteById(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
