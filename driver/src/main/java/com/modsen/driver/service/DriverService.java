package com.modsen.driver.service;

import com.modsen.driver.dto.DriverCreate;
import com.modsen.driver.dto.DriverResponse;
import com.modsen.driver.dto.DriverUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DriverService {

    Page<DriverResponse> findAll(Pageable pageable);

    DriverResponse findById(Integer id);

    DriverResponse update(Integer id, DriverUpdate request);

    DriverResponse create(DriverCreate request);

    void deleteById(Integer id);
}
