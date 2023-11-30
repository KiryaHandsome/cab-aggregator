package com.modsen.payment.service.impl;

import com.modsen.payment.dto.PaymentEvent;
import com.modsen.payment.dto.RideInfo;
import com.modsen.payment.exception.BalanceNotFoundException;
import com.modsen.payment.exception.LowBalanceException;
import com.modsen.payment.model.Balance;
import com.modsen.payment.repository.BalanceRepository;
import com.modsen.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final BalanceRepository balanceRepository;

    @Override
    public PaymentEvent payForRide(RideInfo ride) {
        Balance balance = balanceRepository.findByPassengerId(ride.getPassengerId())
                .orElseThrow(() -> new BalanceNotFoundException(
                        String.format("Balance of passenger with id=%d doesn't exist.", ride.getPassengerId())
                ));
        if (balance.getAmount() < ride.getCost()) {
            throw lowBalanceException(ride);
        }
        Float resultAmount = balance.getAmount() - ride.getCost();
        balance.setAmount(resultAmount);
        balanceRepository.save(balance);
        return new PaymentEvent(
                String.format("Ride with id=%s successfully paid", ride.getId())
        );
    }

    private RuntimeException lowBalanceException(RideInfo ride) {
        String message = String.format(
                "The balance of passenger with id=%d doesn't have enough money to pay for ride with cost %.2f",
                ride.getPassengerId(), ride.getCost()
        );
        return new LowBalanceException(message);
    }
}
