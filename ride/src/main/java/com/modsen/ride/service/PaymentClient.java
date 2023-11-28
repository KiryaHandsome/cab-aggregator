package com.modsen.ride.service;


import com.modsen.ride.dto.RideDto;
import com.modsen.ride.dto.response.PaymentInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "${payment.name}", url = "${payment.url}")
public interface PaymentClient {

    @PostMapping("${payment.pay_uri}")
    ResponseEntity<PaymentInfo> pay(RideDto ride);
}
