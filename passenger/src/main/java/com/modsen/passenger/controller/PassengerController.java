package com.modsen.passenger.controller;

import com.modsen.passenger.controller.openapi.PassengerControllerOpenApi;
import com.modsen.passenger.dto.request.PassengerCreate;
import com.modsen.passenger.dto.response.PassengerResponse;
import com.modsen.passenger.dto.request.PassengerUpdate;
import com.modsen.passenger.service.PassengerService;
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
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController implements PassengerControllerOpenApi {

    private final PassengerService passengerService;

    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getPassenger(@PathVariable Integer id) {
        PassengerResponse response = passengerService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<PassengerResponse>> getAllPassengers(Pageable pageable) {
        Page<PassengerResponse> response = passengerService.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PassengerResponse> updatePassenger(
            @PathVariable Integer id,
            @Valid @RequestBody PassengerUpdate request
    ) {
        PassengerResponse response = passengerService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PassengerResponse> createPassenger(@Valid @RequestBody PassengerCreate request) {
        PassengerResponse response = passengerService.create(request);
        return ResponseEntity
                .created(URI.create("/api/v1/passengers/" + response.getId()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePassenger(@PathVariable Integer id) {
        passengerService.deleteById(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
