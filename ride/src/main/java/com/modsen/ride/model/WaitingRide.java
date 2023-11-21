package com.modsen.ride.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "waiting_ride_requests")
public class WaitingRide {

    @Id
    private String id;
    private Integer passengerId;
    private String from;
    private String to;
}
