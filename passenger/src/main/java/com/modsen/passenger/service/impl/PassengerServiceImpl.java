package com.modsen.passenger.service.impl;

import com.modsen.passenger.dto.PassengerCreate;
import com.modsen.passenger.dto.PassengerResponse;
import com.modsen.passenger.dto.PassengerUpdate;
import com.modsen.passenger.exception.EmailAlreadyExistsException;
import com.modsen.passenger.exception.PassengerNotFoundException;
import com.modsen.passenger.exception.PhoneNumberAlreadyExistsException;
import com.modsen.passenger.mapper.PassengerMapper;
import com.modsen.passenger.model.Passenger;
import com.modsen.passenger.model.Rating;
import com.modsen.passenger.repository.PassengerRepository;
import com.modsen.passenger.repository.RatingRepository;
import com.modsen.passenger.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerMapper mapper;
    private final RatingRepository ratingRepository;
    private final PassengerRepository passengerRepository;

    @Override
    @Transactional(readOnly = true)
    public PassengerResponse findById(Integer id) {
        return passengerRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new PassengerNotFoundException("Passenger with such id not found. id=" + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PassengerResponse> findAll(Pageable pageable) {
        return passengerRepository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    public PassengerResponse update(Integer id, PassengerUpdate request) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException("Passenger with such not found. id=" + id));
        throwIfEmailOrPhoneAlreadyExist(request.email(), request.phoneNumber());
        updateIfNotNull(request, passenger);
        passengerRepository.save(passenger);
        return mapper.toResponse(passenger);
    }


    @Override
    public void deleteById(Integer id) {
        passengerRepository.deleteById(id);
    }

    @Override
    public PassengerResponse create(PassengerCreate request) {
        Passenger passenger = mapper.toModel(request);
        throwIfEmailOrPhoneAlreadyExist(request.getEmail(), request.getPhoneNumber());
        passengerRepository.save(passenger);
        Rating rating = new Rating(null, 0, 0f, passenger);
        ratingRepository.save(rating);
        return mapper.toResponse(passenger);
    }

    private void throwIfEmailOrPhoneAlreadyExist(String email, String phoneNumber) {
        passengerRepository.findByEmail(email)
                .ifPresent(ignored -> {
                    throw new EmailAlreadyExistsException("Email already exists. email=" + email);
                });
        passengerRepository.findByPhoneNumber(phoneNumber)
                .ifPresent(ignored -> {
                    throw new PhoneNumberAlreadyExistsException("Phone number already exists. number=" + phoneNumber);
                });
    }

    private void updateIfNotNull(PassengerUpdate request, Passenger passenger) {
        if (request.name() != null)
            passenger.setName(request.name());
        if (request.surname() != null)
            passenger.setSurname(request.surname());
        if (request.email() != null)
            passenger.setEmail(request.email());
        if (request.phoneNumber() != null)
            passenger.setPhoneNumber(request.phoneNumber());
    }
}
