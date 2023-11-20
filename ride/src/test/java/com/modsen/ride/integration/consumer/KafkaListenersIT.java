package com.modsen.ride.integration.consumer;

import com.modsen.ride.dto.RideEvent;
import com.modsen.ride.integration.BaseIntegrationTest;
import com.modsen.ride.model.Ride;
import com.modsen.ride.model.WaitingRide;
import com.modsen.ride.repository.RideRepository;
import com.modsen.ride.repository.TestRideRepository;
import com.modsen.ride.service.impl.KafkaProducer;
import com.modsen.ride.util.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@ActiveProfiles("test")
@SpringBootTest
public class KafkaListenersIT extends BaseIntegrationTest {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private TestRideRepository testRideRepository;

    @Autowired
    private RideRepository rideRepository;

    @Test
    void rideStartedListener_shouldRemoveRideFromWaitingAndAddToRides() {
        RideEvent event = TestData.rideEvent();
        kafkaProducer.sendMessage("ride-started", event);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Optional<WaitingRide> waitingRide = testRideRepository.findWaitingRideByPassengerId(event.getPassengerId());
                    Optional<Ride> createdRide = rideRepository.findByPassengerIdAndDriverIdAndFinishTimeIsNull(
                            event.getPassengerId(), event.getDriverId()
                    );
                    assertThat(waitingRide).isNotPresent();
                    assertThat(createdRide).isPresent();
                });
    }

}
