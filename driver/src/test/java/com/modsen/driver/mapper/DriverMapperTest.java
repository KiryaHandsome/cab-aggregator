package com.modsen.driver.mapper;

import com.modsen.driver.dto.request.DriverCreate;
import com.modsen.driver.dto.request.DriverUpdate;
import com.modsen.driver.dto.response.DriverResponse;
import com.modsen.driver.model.Driver;
import com.modsen.driver.model.Status;
import com.modsen.driver.util.TestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class DriverMapperTest {

    private static DriverMapper driverMapper;

    @BeforeAll
    static void setUp() {
        driverMapper = Mappers.getMapper(DriverMapper.class);
    }

    @Test
    void toResponse_shouldReturnExpectedValues() {
        Driver driver = TestData.defaultDriver();
        DriverResponse expected = TestData.defaultDriverResponse();

        DriverResponse actual = driverMapper.toResponse(driver);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toModel_shouldReturnExpectedValues() {
        DriverCreate request = TestData.defaultDriverCreate();
        Driver expected = new Driver(
                null,
                TestData.NAME,
                TestData.SURNAME,
                TestData.EMAIL,
                TestData.PHONE_NUMBER,
                null
        );

        Driver actual = driverMapper.toModel(request);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateIfNotNull_shouldNotUpdateFields() {
        DriverUpdate source = TestData.nullDriverUpdate();
        Driver expected = TestData.defaultDriver();
        Driver target = TestData.defaultDriver();

        driverMapper.mapIfNotNull(source, target);

        assertThat(target).isEqualTo(expected);
    }

    @Test
    void updateIfNotNull_shouldUpdateFields() {
        DriverUpdate source = TestData.newDriverUpdate();
        Driver target = TestData.defaultDriver();
        Driver expected = new Driver(
                TestData.DRIVER_ID,
                TestData.NEW_NAME,
                TestData.NEW_SURNAME,
                TestData.NEW_EMAIL,
                TestData.NEW_PHONE_NUMBER,
                TestData.NEW_STATUS
        );

        driverMapper.mapIfNotNull(source, target);

        assertThat(target).isEqualTo(expected);
    }
}