package com.modsen.ride.dto.request;

import com.modsen.ride.dto.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdate {

    private DriverStatus status;
}
