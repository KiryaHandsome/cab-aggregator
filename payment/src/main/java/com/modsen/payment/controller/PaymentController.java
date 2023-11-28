package com.modsen.payment.controller;

import com.modsen.payment.dto.PaymentInfo;
import com.modsen.payment.dto.RideInfo;
import com.modsen.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/ride")
    public ResponseEntity<PaymentInfo> payForRide(@RequestBody RideInfo rideInfo) {
        PaymentInfo response = paymentService.payForRide(rideInfo);
        return ResponseEntity.ok(response);
    }
}
