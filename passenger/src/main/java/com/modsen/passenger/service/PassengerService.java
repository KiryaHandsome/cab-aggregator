package com.modsen.passenger.service;

import com.modsen.passenger.dto.PassengerCreate;
import com.modsen.passenger.dto.PassengerResponse;
import com.modsen.passenger.dto.PassengerUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PassengerService {

    PassengerResponse findById(Integer id);

    Page<PassengerResponse> findAll(Pageable pageable);

    PassengerResponse update(Integer id, PassengerUpdate request);

    void deleteById(Integer id);

    PassengerResponse create(PassengerCreate request);
}
