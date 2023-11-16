package com.modsen.ride.repository;

import com.modsen.ride.model.WaitingRide;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WaitingRideRepository extends MongoRepository<WaitingRide, String> {

    void deleteByPassengerId(Integer passengerId);
}
