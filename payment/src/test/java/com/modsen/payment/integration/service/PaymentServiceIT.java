package com.modsen.payment.integration.service;

import com.modsen.payment.dto.RideInfo;
import com.modsen.payment.integration.BaseIntegrationTest;
import com.modsen.payment.model.Balance;
import com.modsen.payment.repository.BalanceRepository;
import com.modsen.payment.service.PaymentService;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(
        scripts = {"classpath:sql/delete-data.sql", "classpath:sql/create-data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@SpringBootTest
public class PaymentServiceIT extends BaseIntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BalanceRepository balanceRepository;

    @Test
    public void payForRide_shouldUpdateBalanceOnlyOnce() {
        Integer passengerId = 1;
        RideInfo rideInfo = new RideInfo(null, null, passengerId, null, null, 10.f, null, null);
        Float passengerBalance = 90.88f;
        rideInfo.setPassengerId(passengerId);
        Float expectedAmount = passengerBalance - rideInfo.getCost();

        // Simulate two threads trying to pay for the ride simultaneously
        Runnable payForRideTask = () -> {
            try {
                paymentService.payForRide(rideInfo);
            } catch (OptimisticLockingFailureException e) {
                System.out.println("Caught the OptimisticLockingFailureException");
            }
        };
        List<Thread> threads = List.of(new Thread(payForRideTask), new Thread(payForRideTask));
        threads.forEach(Thread::start);
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Balance updatedBalance = balanceRepository.findByPassengerId(passengerId).orElse(null);

        assertThat(updatedBalance).isNotNull();
        assertThat(updatedBalance.getAmount()).isCloseTo(expectedAmount, Offset.offset(0.01f));
    }
}
