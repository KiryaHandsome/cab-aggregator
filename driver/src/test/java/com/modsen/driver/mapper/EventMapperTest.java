package com.modsen.driver.mapper;

import com.modsen.driver.dto.event.RideEvent;
import com.modsen.driver.dto.event.RideOrdered;
import com.modsen.driver.util.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class EventMapperTest {

    private EventMapper mapper;


    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(EventMapper.class);
    }

    @Test
    void toRideEvent_shouldReturnMappedEntity() {
        RideOrdered rideOrdered = TestData.defaultRideOrdered();

        RideEvent actual = mapper.toRideEvent(rideOrdered);

        assertThat(actual).isNotNull();
        assertThat(actual.getDriverId()).isNull();
        assertThat(actual.getPassengerId()).isEqualTo(TestData.PASSENGER_ID);
        assertThat(actual.getFrom()).isEqualTo(TestData.START_LOCATION);
        assertThat(actual.getTo()).isEqualTo(TestData.FINISH_LOCATION);
    }
}