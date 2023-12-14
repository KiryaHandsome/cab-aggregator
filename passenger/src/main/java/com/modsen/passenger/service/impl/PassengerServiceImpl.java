package com.modsen.passenger.service.impl;

import com.modsen.passenger.dto.request.PassengerCreate;
import com.modsen.passenger.dto.request.PassengerUpdate;
import com.modsen.passenger.dto.response.PassengerResponse;
import com.modsen.passenger.exception.EmailAlreadyExistsException;
import com.modsen.passenger.exception.PassengerNotFoundException;
import com.modsen.passenger.exception.PhoneNumberAlreadyExistsException;
import com.modsen.passenger.mapper.PassengerMapper;
import com.modsen.passenger.model.Passenger;
import com.modsen.passenger.repository.PassengerRepository;
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
    private final PassengerRepository passengerRepository;

    @Override
    @Transactional(readOnly = true)
    public PassengerResponse findById(Integer id) {
        return passengerRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new PassengerNotFoundException("exception.passenger_not_found", id));
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
                .orElseThrow(() -> new PassengerNotFoundException("exception.passenger_not_found", id));
        throwIfEmailOrPhoneAlreadyExist(request.getEmail(), request.getPhoneNumber());
        mapper.mapIfNotNull(request, passenger);
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
        return mapper.toResponse(passenger);
    }

    private void throwIfEmailOrPhoneAlreadyExist(String email, String phoneNumber) {
        passengerRepository.findByEmail(email)
                .ifPresent(ignored -> {
                    throw new EmailAlreadyExistsException("exception.email_already_exists", email);
                });
        passengerRepository.findByPhoneNumber(phoneNumber)
                .ifPresent(ignored -> {
                    throw new PhoneNumberAlreadyExistsException("exception.phone_already_exists", phoneNumber);
                });
    }
}
