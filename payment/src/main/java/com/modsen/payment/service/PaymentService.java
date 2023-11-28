package com.modsen.payment.service;

import com.modsen.payment.dto.PaymentInfo;
import com.modsen.payment.dto.RideInfo;

public interface PaymentService {

    PaymentInfo payForRide(RideInfo rideInfo);
}
