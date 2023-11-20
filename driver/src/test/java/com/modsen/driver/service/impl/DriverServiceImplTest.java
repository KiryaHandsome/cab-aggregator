package com.modsen.driver.service.impl;

import com.modsen.driver.dto.response.DriverResponse;
import com.modsen.driver.exception.DriverNotFoundException;
import com.modsen.driver.exception.EmailAlreadyExistsException;
import com.modsen.driver.exception.PhoneNumberAlreadyExistsException;
import com.modsen.driver.mapper.DriverMapper;
import com.modsen.driver.model.Driver;
import com.modsen.driver.repository.DriverRepository;
import com.modsen.driver.repository.RatingRepository;
import com.modsen.driver.util.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DriverServiceImplTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private DriverMapper driverMapper;

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverServiceImpl driverService;

    @Test
    void findAll_shouldReturnExpectedPage() {
        List<Driver> content = List.of(
                TestData.defaultDriver(),
                TestData.defaultDriver()
        );
        Pageable pageable = Pageable.ofSize(2);

        doReturn(new PageImpl<>(content))
                .when(driverRepository)
                .findAll(pageable);
        doReturn(TestData.defaultDriverResponse())
                .when(driverMapper)
                .toResponse(TestData.defaultDriver());

        Page<DriverResponse> actual = driverService.findAll(pageable);

        assertThat(actual).hasSize(2);
        assertThat(actual).contains(TestData.defaultDriverResponse());

        verify(driverRepository).findAll(eq(pageable));
        verify(driverMapper, times(2)).toResponse(TestData.defaultDriver());
    }


    @Test
    void findById_shouldReturnExpectedDriver() {
        doReturn(Optional.of(TestData.defaultDriver()))
                .when(driverRepository)
                .findById(TestData.DRIVER_ID);
        doReturn(TestData.defaultDriverResponse())
                .when(driverMapper)
                .toResponse(TestData.defaultDriver());

        DriverResponse actual = driverService.findById(TestData.DRIVER_ID);

        assertThat(actual).isEqualTo(TestData.defaultDriverResponse());

        verify(driverRepository).findById(eq(TestData.DRIVER_ID));
        verify(driverMapper).toResponse(eq(TestData.defaultDriver()));
    }

    @Test
    void findById_shouldThrowDriverNotFoundException() {
        doReturn(Optional.empty())
                .when(driverRepository)
                .findById(TestData.DRIVER_ID);

        assertThrows(DriverNotFoundException.class,
                () -> driverService.findById(TestData.DRIVER_ID));

        verify(driverRepository).findById(eq(TestData.DRIVER_ID));
    }

    @Test
    void update_shouldReturnUpdatedDriver() {
        doReturn(Optional.of(TestData.defaultDriver()))
                .when(driverRepository)
                .findById(TestData.DRIVER_ID);
        doReturn(TestData.defaultDriverResponse())
                .when(driverMapper)
                .toResponse(TestData.defaultDriver());

        DriverResponse actual = driverService.update(TestData.DRIVER_ID, TestData.defaultDriverUpdate());

        assertThat(actual).isEqualTo(TestData.defaultDriverResponse());

        verify(driverRepository).findById(eq(TestData.DRIVER_ID));
        verify(driverMapper).updateIfNotNull(eq(TestData.defaultDriverUpdate()), eq(TestData.defaultDriver()));
        verify(driverRepository).save(eq(TestData.defaultDriver()));
    }

    @Test
    void update_shouldThrowDriverNotFoundException() {
        doReturn(Optional.empty())
                .when(driverRepository)
                .findById(TestData.DRIVER_ID);

        assertThrows(DriverNotFoundException.class,
                () -> driverService.update(TestData.DRIVER_ID, TestData.defaultDriverUpdate()));

        verify(driverRepository).findById(eq(TestData.DRIVER_ID));
    }

    @Test
    void update_shouldEmailAlreadyExistsException() {
        doReturn(Optional.of(TestData.defaultDriver()))
                .when(driverRepository)
                .findById(TestData.DRIVER_ID);
        doReturn(Optional.of(new Driver()))
                .when(driverRepository)
                .findByEmail(TestData.EMAIL);

        assertThrows(EmailAlreadyExistsException.class,
                () -> driverService.update(TestData.DRIVER_ID, TestData.defaultDriverUpdate()));

        verify(driverRepository).findById(eq(TestData.DRIVER_ID));
        verify(driverRepository).findByEmail(eq(TestData.EMAIL));
    }

    @Test
    void update_shouldPhoneNumberAlreadyExistsException() {
        doReturn(Optional.of(TestData.defaultDriver()))
                .when(driverRepository)
                .findById(TestData.DRIVER_ID);
        doReturn(Optional.of(new Driver()))
                .when(driverRepository)
                .findByPhoneNumber(TestData.PHONE_NUMBER);

        assertThrows(PhoneNumberAlreadyExistsException.class,
                () -> driverService.update(TestData.DRIVER_ID, TestData.defaultDriverUpdate()));

        verify(driverRepository).findById(eq(TestData.DRIVER_ID));
        verify(driverRepository).findByPhoneNumber(eq(TestData.PHONE_NUMBER));
    }

    @Test
    void create_shouldReturnCreatedDriver() {
        doReturn(TestData.defaultDriver())
                .when(driverMapper)
                .toModel(TestData.defaultDriverCreate());
        doReturn(Optional.empty())
                .when(driverRepository)
                .findByEmail(TestData.EMAIL);
        doReturn(Optional.empty())
                .when(driverRepository)
                .findByPhoneNumber(TestData.PHONE_NUMBER);
        doReturn(TestData.defaultDriverResponse())
                .when(driverMapper)
                .toResponse(TestData.defaultDriver());

        DriverResponse actual = driverService.create(TestData.defaultDriverCreate());

        assertThat(actual).isEqualTo(TestData.defaultDriverResponse());

        verify(ratingRepository).save(eq(TestData.initialRating()));
        verify(driverMapper).toResponse(eq(TestData.defaultDriver()));
        verify(driverMapper).toModel(eq(TestData.defaultDriverCreate()));
        verify(driverRepository).findByEmail(eq(TestData.EMAIL));
        verify(driverRepository).findByPhoneNumber(eq(TestData.PHONE_NUMBER));
    }

    @Test
    void create_shouldEmailAlreadyExistsException() {
        doReturn(Optional.of(new Driver()))
                .when(driverRepository)
                .findByEmail(TestData.EMAIL);

        assertThrows(EmailAlreadyExistsException.class,
                () -> driverService.create(TestData.defaultDriverCreate()));

        verify(driverRepository).findByEmail(eq(TestData.EMAIL));
    }

    @Test
    void create_shouldPhoneNumberAlreadyExistsException() {
        doReturn(Optional.of(new Driver()))
                .when(driverRepository)
                .findByPhoneNumber(TestData.PHONE_NUMBER);

        assertThrows(PhoneNumberAlreadyExistsException.class,
                () -> driverService.create(TestData.defaultDriverCreate()));

        verify(driverRepository).findByPhoneNumber(eq(TestData.PHONE_NUMBER));
    }

    @Test
    void deleteById_shouldCallDeleteMethod() {
        driverService.deleteById(TestData.DRIVER_ID);

        verify(driverRepository).deleteById(eq(TestData.DRIVER_ID));
    }
}