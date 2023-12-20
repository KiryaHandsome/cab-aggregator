package com.modsen.payment.service.impl;

import com.modsen.payment.dto.PaymentEvent;
import com.modsen.payment.dto.RideInfo;
import com.modsen.payment.exception.BalanceNotFoundException;
import com.modsen.payment.model.Balance;
import com.modsen.payment.model.PaymentStatus;
import com.modsen.payment.repository.BalanceRepository;
import com.modsen.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final BalanceRepository balanceRepository;

    @Override
    @Retryable(
            maxAttempts = 5,
            backoff = @Backoff(1000),
            retryFor = ObjectOptimisticLockingFailureException.class
    )
    public PaymentEvent payForRide(RideInfo ride) {
        Balance balance = balanceRepository.findByPassengerId(ride.getPassengerId())
                .orElseThrow(() -> new BalanceNotFoundException(
                        String.format("Balance of passenger with id=%d doesn't exist.", ride.getPassengerId())
                ));
        if (balance.getAmount() < ride.getCost()) {
            log.info("Payment for ride with id={} failed. Not enough money.", ride.getId());
            return PaymentEvent.builder()
                    .rideId(ride.getId())
                    .status(PaymentStatus.FAILED)
                    .build();
        }
        Float resultAmount = balance.getAmount() - ride.getCost();
        balance.setAmount(resultAmount);
        balanceRepository.save(balance);
        return PaymentEvent.builder()
                .rideId(ride.getId())
                .status(PaymentStatus.PAID)
                .build();
    }
}
