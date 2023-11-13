package com.modsen.driver.repository;

import com.modsen.driver.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

    Optional<Rating> findByDriverId(Integer driverId);
}
