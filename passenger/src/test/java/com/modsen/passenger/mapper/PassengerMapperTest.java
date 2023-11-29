package com.modsen.passenger.mapper;

import com.modsen.passenger.dto.request.PassengerCreate;
import com.modsen.passenger.dto.request.PassengerUpdate;
import com.modsen.passenger.dto.response.PassengerResponse;
import com.modsen.passenger.model.Passenger;
import com.modsen.passenger.util.TestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class PassengerMapperTest {

    private static PassengerMapper mapper;

    @BeforeAll
    static void setUp() {
        mapper = Mappers.getMapper(PassengerMapper.class);
    }

    @Test
    void mapper_isNotNull() {
        assertThat(mapper).isNotNull();
    }

    @Test
    void toResponse_should_return_mappedPassenger() {
        Passenger passenger = TestData.defaultPassenger();
        PassengerResponse expected = TestData.defaultPassengerResponse();

        PassengerResponse actual = mapper.toResponse(passenger);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toModel_shouldReturnMappedPassenger() {
        PassengerCreate dto = TestData.defaultPassengerCreate();
        Passenger expected = TestData.defaultPassenger();
        expected.setId(null);

        Passenger actual = mapper.toModel(dto);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateIfNotNull_shouldReturnUpdatedPassengerWithAllNewValues() {
        PassengerUpdate request = TestData.newPassengerUpdate();
        Passenger expected = TestData.newPassengerWithDefaultId();
        Passenger target = TestData.defaultPassenger();

        mapper.mapIfNotNull(request, target);

        assertThat(target).isEqualTo(expected);
    }

    @Test
    void updateIfNotNull_withNullsShouldNotUpdatePassenger() {
        PassengerUpdate request = new PassengerUpdate(
                null, null, null, null
        );
        Passenger expected = TestData.defaultPassenger();
        Passenger target = TestData.defaultPassenger();

        mapper.mapIfNotNull(request, target);

        assertThat(target).isEqualTo(expected);
    }
}