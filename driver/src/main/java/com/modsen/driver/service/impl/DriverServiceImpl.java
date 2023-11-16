package com.modsen.driver.service.impl;

import com.modsen.driver.dto.request.DriverCreate;
import com.modsen.driver.dto.request.DriverUpdate;
import com.modsen.driver.dto.response.DriverResponse;
import com.modsen.driver.exception.DriverNotFoundException;
import com.modsen.driver.exception.EmailAlreadyExistsException;
import com.modsen.driver.exception.PhoneNumberAlreadyExistsException;
import com.modsen.driver.mapper.DriverMapper;
import com.modsen.driver.model.Driver;
import com.modsen.driver.model.Rating;
import com.modsen.driver.model.Status;
import com.modsen.driver.repository.DriverRepository;
import com.modsen.driver.repository.RatingRepository;
import com.modsen.driver.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverMapper mapper;
    private final RatingRepository ratingRepository;
    private final DriverRepository driverRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<DriverResponse> findAll(Pageable pageable) {
        return driverRepository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DriverResponse findById(Integer id) {
        return driverRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new DriverNotFoundException("Driver with such id not found. id=" + id));
    }

    @Override
    public DriverResponse update(Integer id, DriverUpdate request) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException("Driver with such not found. id=" + id));
        throwIfEmailOrPhoneAlreadyExist(request.getEmail(), request.getPhoneNumber());
        updateIfNotNull(request, driver);
        driverRepository.save(driver);
        return mapper.toResponse(driver);
    }

    @Override
    public DriverResponse create(DriverCreate request) {
        Driver driver = mapper.toModel(request);
        throwIfEmailOrPhoneAlreadyExist(request.getEmail(), request.getPhoneNumber());
        driver.setStatus(Status.OFFLINE);
        driverRepository.save(driver);
        Rating rating = new Rating(null, 0, 0f, driver);
        ratingRepository.save(rating);
        return mapper.toResponse(driver);
    }

    @Override
    public void deleteById(Integer id) {
        driverRepository.deleteById(id);
    }

    private void throwIfEmailOrPhoneAlreadyExist(String email, String phoneNumber) {
        driverRepository.findByEmail(email)
                .ifPresent(ignored -> {
                    throw new EmailAlreadyExistsException("Email already exists. email=" + email);
                });
        driverRepository.findByPhoneNumber(phoneNumber)
                .ifPresent(ignored -> {
                    throw new PhoneNumberAlreadyExistsException("Phone number already exists. number=" + phoneNumber);
                });
    }

    private void updateIfNotNull(DriverUpdate request, Driver driver) {
        if (request.getName() != null)
            driver.setName(request.getName());
        if (request.getSurname() != null)
            driver.setSurname(request.getSurname());
        if (request.getEmail() != null)
            driver.setEmail(request.getEmail());
        if (request.getPhoneNumber() != null)
            driver.setPhoneNumber(request.getPhoneNumber());
        if (request.getStatus() != null)
            driver.setStatus(request.getStatus());
    }
}
