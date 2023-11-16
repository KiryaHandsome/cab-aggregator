package com.modsen.ride.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rides")
public class Ride {

    @Id
    private String id;
    private Integer driverId;
    private Integer passengerId;
    private String from;
    private String to;

    @Version
    private Integer version;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
}
