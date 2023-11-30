package com.modsen.payment.service;

import com.modsen.payment.dto.PaymentEvent;
import com.modsen.payment.dto.RideInfo;

public interface PaymentService {

    PaymentEvent payForRide(RideInfo rideInfo);
}
