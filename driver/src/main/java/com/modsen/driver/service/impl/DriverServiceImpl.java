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
import org.springframework.context.MessageSource;
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
                .orElseThrow(() -> new DriverNotFoundException("exception.driver_not_found", id));
    }

    @Override
    public DriverResponse update(Integer id, DriverUpdate request) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException("exception.driver_not_found", id));
        throwIfEmailOrPhoneAlreadyExist(request.getEmail(), request.getPhoneNumber());
        mapper.mapIfNotNull(request, driver);
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
                    throw new EmailAlreadyExistsException("exception.email_already_exists", email);
                });
        driverRepository.findByPhoneNumber(phoneNumber)
                .ifPresent(ignored -> {
                    throw new PhoneNumberAlreadyExistsException("exception.phone_already_exists", phoneNumber);
                });
    }
}
