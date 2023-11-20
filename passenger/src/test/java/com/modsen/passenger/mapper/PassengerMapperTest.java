package com.modsen.passenger.mapper;

import com.modsen.passenger.dto.request.PassengerCreate;
import com.modsen.passenger.dto.request.PassengerUpdate;
import com.modsen.passenger.dto.response.PassengerResponse;
import com.modsen.passenger.model.Passenger;
import com.modsen.passenger.util.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class PassengerMapperTest {

    private PassengerMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(PassengerMapper.class);
    }

    @Test
    void check_mapper_isNotNull() {
        assertThat(mapper).isNotNull();
    }

    @Test
    void check_toResponse_should_return_mappedPassenger() {
        Passenger passenger = TestData.defaultPassenger();

        PassengerResponse actual = mapper.toResponse(passenger);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(passenger.getId());
        assertThat(actual.getName()).isEqualTo(passenger.getName());
        assertThat(actual.getSurname()).isEqualTo(passenger.getSurname());
        assertThat(actual.getPhoneNumber()).isEqualTo(passenger.getPhoneNumber());
        assertThat(actual.getEmail()).isEqualTo(passenger.getEmail());
    }

    @Test
    void check_toModel_should_return_mappedPassenger() {
        PassengerCreate dto = TestData.defaultPassengerCreate();

        Passenger actual = mapper.toModel(dto);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNull();
        assertThat(actual.getName()).isEqualTo(dto.getName());
        assertThat(actual.getSurname()).isEqualTo(dto.getSurname());
        assertThat(actual.getPhoneNumber()).isEqualTo(dto.getPhoneNumber());
        assertThat(actual.getEmail()).isEqualTo(dto.getEmail());
    }

    @Test
    void check_updateIfNotNull_should_return_updatedPassengerWithAllNewValues() {
        PassengerUpdate request = new PassengerUpdate(
                TestData.NEW_NAME,
                TestData.NEW_SURNAME,
                TestData.NEW_EMAIL,
                TestData.NEW_PHONE_NUMBER
        );

        Passenger actual = new Passenger(
                TestData.PASSENGER_ID,
                TestData.NAME,
                TestData.SURNAME,
                TestData.EMAIL,
                TestData.PHONE_NUMBER
        );

        mapper.updateIfNotNull(request, actual);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(TestData.PASSENGER_ID);
        assertThat(actual.getName()).isEqualTo(TestData.NEW_NAME);
        assertThat(actual.getSurname()).isEqualTo(TestData.NEW_SURNAME);
        assertThat(actual.getPhoneNumber()).isEqualTo(TestData.NEW_PHONE_NUMBER);
        assertThat(actual.getEmail()).isEqualTo(TestData.NEW_EMAIL);
    }

    @Test
    void check_updateIfNotNull_withNulls_should_not_updatePassenger() {
        PassengerUpdate request = new PassengerUpdate(
                null, null, null, null
        );

        Passenger actual = new Passenger(
                TestData.PASSENGER_ID,
                TestData.NAME,
                TestData.SURNAME,
                TestData.EMAIL,
                TestData.PHONE_NUMBER
        );

        mapper.updateIfNotNull(request, actual);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(TestData.PASSENGER_ID);
        assertThat(actual.getName()).isEqualTo(TestData.NAME);
        assertThat(actual.getSurname()).isEqualTo(TestData.SURNAME);
        assertThat(actual.getPhoneNumber()).isEqualTo(TestData.PHONE_NUMBER);
        assertThat(actual.getEmail()).isEqualTo(TestData.EMAIL);
    }
}