package com.modsen.ride.repository;

import com.modsen.ride.model.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RideRepository extends MongoRepository<Ride, String> {

    Page<Ride> findByPassengerId(Integer passengerId, Pageable pageable);

    Page<Ride> findByDriverId(Integer driverId, Pageable pageable);
}
