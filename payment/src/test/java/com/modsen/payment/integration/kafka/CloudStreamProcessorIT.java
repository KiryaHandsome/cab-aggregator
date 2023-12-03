package com.modsen.payment.integration.kafka;

import com.modsen.payment.dto.PaymentEvent;
import com.modsen.payment.dto.RideInfo;
import com.modsen.payment.integration.BaseIntegrationTest;
import com.modsen.payment.model.Balance;
import com.modsen.payment.model.PaymentStatus;
import com.modsen.payment.repository.BalanceRepository;
import com.modsen.payment.util.JsonUtil;
import com.modsen.payment.util.TestData;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Sql(
        scripts = {"classpath:sql/delete-data.sql", "classpath:sql/create-data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@SpringBootTest
public class CloudStreamProcessorIT extends BaseIntegrationTest {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private KafkaTemplate<String, RideInfo> kafkaTemplate;

    @Test
    void messageProcessing_shouldReceiveFromRidePaymentThenUpdatePassengerBalanceAndSendResultToPaymentResultTopic() {
        Integer passengerId = 1;
        RideInfo rideInfo = new RideInfo(TestData.RIDE_ID, null, passengerId, null, null, 10.f, null, null);
        Float passengerBalance = 90.88f;
        rideInfo.setPassengerId(passengerId);
        Float expectedAmount = passengerBalance - rideInfo.getCost();
        PaymentEvent expectedPaymentEvent = PaymentEvent.builder()
                .status(PaymentStatus.PAID)
                .rideId(rideInfo.getId())
                .build();

        kafkaTemplate.send("ride-payment", rideInfo);

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    Optional<Balance> actual = balanceRepository.findById(rideInfo.getPassengerId());
                    assertThat(actual.get().getAmount()).isCloseTo(expectedAmount, Offset.offset(.001f));
                });

        KafkaConsumer<String, String> consumer =
                setUpKafkaConsumer("payment-result");

        ConsumerRecords<String, String> records = consumer.poll(Duration.of(10, TimeUnit.SECONDS.toChronoUnit()));
        assertThat(records.count()).isEqualTo(1);
        records.forEach(r -> assertThat(r.value()).isEqualTo(JsonUtil.toJson(expectedPaymentEvent)));
    }

    private KafkaConsumer<String, String> setUpKafkaConsumer(String topicName) {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(ImmutableMap.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG, topicName + "cloud-stream-processor-test",
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
        ), new StringDeserializer(), new StringDeserializer());
        consumer.subscribe(Collections.singletonList(topicName));
        return consumer;
    }
}
