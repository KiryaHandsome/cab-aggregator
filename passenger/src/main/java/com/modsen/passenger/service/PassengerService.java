package com.modsen.passenger.service;

import com.modsen.passenger.dto.request.PassengerCreate;
import com.modsen.passenger.dto.request.PassengerUpdate;
import com.modsen.passenger.dto.response.PassengerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PassengerService {

    PassengerResponse findById(Integer id);

    Page<PassengerResponse> findAll(Pageable pageable);

    PassengerResponse update(Integer id, PassengerUpdate request);

    void deleteById(Integer id);

    PassengerResponse create(PassengerCreate request);
}
