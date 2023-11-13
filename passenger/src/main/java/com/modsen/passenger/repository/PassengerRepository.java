package com.modsen.passenger.repository;

import com.modsen.passenger.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Integer> {

    Optional<Passenger> findByEmail(String email);

    Optional<Passenger> findByPhoneNumber(String phoneNumber);
}
