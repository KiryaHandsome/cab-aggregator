package com.modsen.passenger.service.impl;

import com.modsen.passenger.dto.response.PassengerResponse;
import com.modsen.passenger.exception.EmailAlreadyExistsException;
import com.modsen.passenger.exception.PassengerNotFoundException;
import com.modsen.passenger.exception.PhoneNumberAlreadyExistsException;
import com.modsen.passenger.mapper.PassengerMapper;
import com.modsen.passenger.model.Passenger;
import com.modsen.passenger.repository.PassengerRepository;
import com.modsen.passenger.repository.RatingRepository;
import com.modsen.passenger.util.TestData;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PassengerServiceImplTest {

    @Mock
    private PassengerMapper passengerMapper;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private PassengerServiceImpl passengerService;

    @Test
    void findById_shouldThrowPassengerNotFoundException() {
        doReturn(Optional.empty())
                .when(passengerRepository)
                .findById(TestData.PASSENGER_ID);

        assertThrows(PassengerNotFoundException.class,
                () -> passengerService.findById(TestData.PASSENGER_ID));

        verify(passengerRepository).findById(eq(TestData.PASSENGER_ID));
    }

    @Test
    void findById_shouldReturnExpectedResponse() {
        PassengerResponse expected = TestData.defaultPassengerResponse();
        Passenger passenger = TestData.defaultPassenger();

        doReturn(Optional.of(passenger))
                .when(passengerRepository)
                .findById(TestData.PASSENGER_ID);
        doReturn(expected)
                .when(passengerMapper)
                .toResponse(passenger);

        PassengerResponse actual = passengerService.findById(TestData.PASSENGER_ID);

        assertThat(actual).isEqualTo(expected);

        verify(passengerRepository).findById(eq(TestData.PASSENGER_ID));
        verify(passengerMapper).toResponse(eq(passenger));
    }

    @Test
    void findAll_shouldReturnExpectedPage() {
        List<Passenger> content = List.of(
                TestData.defaultPassenger(),
                TestData.defaultPassenger()
        );
        Pageable pageable = Pageable.ofSize(2);

        doReturn(new PageImpl<>(content))
                .when(passengerRepository)
                .findAll(pageable);
        doReturn(TestData.defaultPassengerResponse())
                .when(passengerMapper)
                .toResponse(TestData.defaultPassenger());

        Page<PassengerResponse> actual = passengerService.findAll(pageable);

        assertThat(actual).hasSize(2);
        assertThat(actual).contains(TestData.defaultPassengerResponse());

        verify(passengerRepository).findAll(eq(pageable));
        verify(passengerMapper, times(2)).toResponse(TestData.defaultPassenger());
    }

    @Test
    void update_shouldThrowPassengerNotFoundException() {
        doReturn(Optional.empty())
                .when(passengerRepository)
                .findById(TestData.PASSENGER_ID);

        assertThrows(PassengerNotFoundException.class,
                () -> passengerService.update(TestData.PASSENGER_ID, null));

        verify(passengerRepository).findById(eq(TestData.PASSENGER_ID));
    }

    @Test
    void update_shouldThrowEmailAlreadyExistsException() {
        doReturn(Optional.of(TestData.defaultPassenger()))
                .when(passengerRepository)
                .findById(TestData.PASSENGER_ID);
        doReturn(Optional.of(TestData.defaultPassenger()))
                .when(passengerRepository)
                .findByEmail(TestData.EMAIL);

        assertThrows(EmailAlreadyExistsException.class,
                () -> passengerService.update(TestData.PASSENGER_ID, TestData.defaultPassengerUpdate()));

        verify(passengerRepository).findById(eq(TestData.PASSENGER_ID));
        verify(passengerRepository).findByEmail(eq(TestData.EMAIL));
    }

    @Test
    void update_shouldThrowPhoneNumberAlreadyExistsException() {
        doReturn(Optional.of(TestData.defaultPassenger()))
                .when(passengerRepository)
                .findById(TestData.PASSENGER_ID);
        doReturn(Optional.of(TestData.defaultPassenger()))
                .when(passengerRepository)
                .findByPhoneNumber(TestData.PHONE_NUMBER);

        assertThrows(PhoneNumberAlreadyExistsException.class,
                () -> passengerService.update(TestData.PASSENGER_ID, TestData.defaultPassengerUpdate()));

        verify(passengerRepository).findById(eq(TestData.PASSENGER_ID));
        verify(passengerRepository).findByPhoneNumber(eq(TestData.PHONE_NUMBER));
    }

    @Test
    void update_shouldSaveUpdatedPassenger() {
        doReturn(Optional.of(TestData.defaultPassenger()))
                .when(passengerRepository)
                .findById(TestData.PASSENGER_ID);
        doReturn(Optional.empty())
                .when(passengerRepository)
                .findByPhoneNumber(TestData.PHONE_NUMBER);
        doReturn(Optional.empty())
                .when(passengerRepository)
                .findByEmail(TestData.EMAIL);

        passengerService.update(TestData.PASSENGER_ID, TestData.defaultPassengerUpdate());

        verify(passengerRepository).findById(eq(TestData.PASSENGER_ID));
        verify(passengerRepository).findByPhoneNumber(eq(TestData.PHONE_NUMBER));
        verify(passengerRepository).findByEmail(eq(TestData.EMAIL));
        verify(passengerMapper).mapIfNotNull(eq(TestData.defaultPassengerUpdate()), eq(TestData.defaultPassenger()));
        verify(passengerRepository).save(eq(TestData.defaultPassenger()));
    }

    @Test
    void deleteById_shouldCallDeleteMethod() {
        doNothing()
                .when(passengerRepository)
                .deleteById(TestData.PASSENGER_ID);

        passengerService.deleteById(TestData.PASSENGER_ID);

        verify(passengerRepository).deleteById(eq(TestData.PASSENGER_ID));
    }

    @Test
    void create_shouldReturnCreatedObject() {
        doReturn(TestData.defaultPassenger())
                .when(passengerMapper)
                .toModel(TestData.defaultPassengerCreate());
        doReturn(Optional.empty())
                .when(passengerRepository)
                .findByEmail(TestData.EMAIL);
        doReturn(Optional.empty())
                .when(passengerRepository)
                .findByPhoneNumber(TestData.PHONE_NUMBER);
        doReturn(TestData.defaultPassengerResponse())
                .when(passengerMapper)
                .toResponse(TestData.defaultPassenger());

        PassengerResponse actual = passengerService.create(TestData.defaultPassengerCreate());

        assertThat(actual).isEqualTo(TestData.defaultPassengerResponse());

        verify(passengerMapper).toModel(eq(TestData.defaultPassengerCreate()));
        verify(passengerMapper).toResponse(eq(TestData.defaultPassenger()));
        verify(passengerRepository).save(TestData.defaultPassenger());
        verify(ratingRepository).save(eq(TestData.initialRating()));
    }

    @Test
    void create_shouldThrowEmailAlreadyExistsException() {
        doReturn(Optional.of(TestData.defaultPassenger()))
                .when(passengerRepository)
                .findByEmail(TestData.EMAIL);

        assertThrows(EmailAlreadyExistsException.class,
                () -> passengerService.create(TestData.defaultPassengerCreate()));

        verify(passengerRepository).findByEmail(eq(TestData.EMAIL));
    }

    @Test
    void create_shouldThrowPhoneNumberAlreadyExistsException() {
        doReturn(Optional.of(TestData.defaultPassenger()))
                .when(passengerRepository)
                .findByPhoneNumber(TestData.PHONE_NUMBER);

        assertThrows(PhoneNumberAlreadyExistsException.class,
                () -> passengerService.create(TestData.defaultPassengerCreate()));

        verify(passengerRepository).findByPhoneNumber(eq(TestData.PHONE_NUMBER));
    }
}