package com.modsen.driver.service;

import com.modsen.driver.dto.request.DriverCreate;
import com.modsen.driver.dto.request.DriverUpdate;
import com.modsen.driver.dto.response.DriverResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DriverService {

    Page<DriverResponse> findAll(Pageable pageable);

    DriverResponse findById(Integer id);

    DriverResponse update(Integer id, DriverUpdate request);

    DriverResponse create(DriverCreate request);

    void deleteById(Integer id);
}
