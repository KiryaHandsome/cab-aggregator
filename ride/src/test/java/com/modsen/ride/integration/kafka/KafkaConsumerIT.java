package com.modsen.ride.integration.kafka;

import com.modsen.ride.dto.PaymentEvent;
import com.modsen.ride.integration.BaseIntegrationTest;
import com.modsen.ride.model.PaymentStatus;
import com.modsen.ride.model.Ride;
import com.modsen.ride.repository.RideRepository;
import com.modsen.ride.util.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
public class KafkaConsumerIT extends BaseIntegrationTest {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    @Test
    void check_consuming_shouldUpdateRidePaymentStatus() {
        Ride ride = TestData.defaultRide();
        ride.setId(null);
        rideRepository.save(ride);
        assertThat(ride.getId()).isNotNull();

        PaymentEvent event = new PaymentEvent(ride.getId(), PaymentStatus.PAID);
        kafkaTemplate.send("payment-result", event);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(20, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Optional<Ride> actual = rideRepository.findById(ride.getId());
                    assertThat(actual.get().getPaymentStatus()).isEqualTo(PaymentStatus.PAID);
                });
    }

}
