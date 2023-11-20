package com.modsen.driver.mapper;

import com.modsen.driver.dto.request.DriverCreate;
import com.modsen.driver.dto.request.DriverUpdate;
import com.modsen.driver.dto.response.DriverResponse;
import com.modsen.driver.model.Driver;
import com.modsen.driver.model.Status;
import com.modsen.driver.util.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class DriverMapperTest {

    private DriverMapper driverMapper;

    @BeforeEach
    void setUp() {
        driverMapper = Mappers.getMapper(DriverMapper.class);
    }

    @Test
    void toResponse_shouldReturnExpectedValues() {
        Driver driver = TestData.defaultDriver();

        DriverResponse actual = driverMapper.toResponse(driver);

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(TestData.NAME);
        assertThat(actual.getSurname()).isEqualTo(TestData.SURNAME);
        assertThat(actual.getEmail()).isEqualTo(TestData.EMAIL);
        assertThat(actual.getPhoneNumber()).isEqualTo(TestData.PHONE_NUMBER);
        assertThat(actual.getStatus()).isEqualTo(Status.OFFLINE);
    }

    @Test
    void toModel_shouldReturnExpectedValues() {
        DriverCreate request = TestData.defaultDriverCreate();

        Driver actual = driverMapper.toModel(request);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNull();
        assertThat(actual.getName()).isEqualTo(TestData.NAME);
        assertThat(actual.getSurname()).isEqualTo(TestData.SURNAME);
        assertThat(actual.getEmail()).isEqualTo(TestData.EMAIL);
        assertThat(actual.getPhoneNumber()).isEqualTo(TestData.PHONE_NUMBER);
    }

    @Test
    void updateIfNotNull_shouldNotUpdateFields() {
        DriverUpdate source = TestData.nullDriverUpdate();
        Driver target = TestData.defaultDriver();

        driverMapper.updateIfNotNull(source, target);

        assertThat(target).isNotNull();
        assertThat(target.getId()).isEqualTo(TestData.DRIVER_ID);
        assertThat(target.getName()).isEqualTo(TestData.NAME);
        assertThat(target.getSurname()).isEqualTo(TestData.SURNAME);
        assertThat(target.getEmail()).isEqualTo(TestData.EMAIL);
        assertThat(target.getPhoneNumber()).isEqualTo(TestData.PHONE_NUMBER);
        assertThat(target.getStatus()).isEqualTo(Status.OFFLINE);
    }

    @Test
    void updateIfNotNull_shouldUpdateFields() {
        DriverUpdate source = TestData.newDriverUpdate();
        Driver target = TestData.defaultDriver();

        driverMapper.updateIfNotNull(source, target);

        assertThat(target).isNotNull();
        assertThat(target.getId()).isEqualTo(TestData.DRIVER_ID);
        assertThat(target.getName()).isEqualTo(TestData.NEW_NAME);
        assertThat(target.getSurname()).isEqualTo(TestData.NEW_SURNAME);
        assertThat(target.getEmail()).isEqualTo(TestData.NEW_EMAIL);
        assertThat(target.getPhoneNumber()).isEqualTo(TestData.NEW_PHONE_NUMBER);
        assertThat(target.getStatus()).isEqualTo(Status.BUSY);
    }
}