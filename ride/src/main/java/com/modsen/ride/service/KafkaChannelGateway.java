package com.modsen.ride.service;

import com.modsen.ride.dto.RideDto;
import com.modsen.ride.dto.request.RideRequest;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "sendToRideOrderedChannel")
public interface KafkaChannelGateway {

    @Gateway(requestChannel = "sendToRideOrderedChannel")
    void sendToRideOrdered(RideRequest request);

    @Gateway(requestChannel = "sendToRidePaymentChannel")
    void sendToRidePayment(RideDto rideInfo);
}
