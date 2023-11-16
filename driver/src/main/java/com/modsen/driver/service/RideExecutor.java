package com.modsen.driver.service;

import com.modsen.driver.dto.event.RideEvent;

public interface RideExecutor {

    void ride(RideEvent event);
}
