package com.modsen.ride.dto.request;

import com.modsen.ride.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {

    private String rideId;
    private PaymentStatus status;
}
