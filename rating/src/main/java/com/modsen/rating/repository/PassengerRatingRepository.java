package com.modsen.rating.repository;

import com.modsen.rating.model.PassengerRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PassengerRatingRepository extends JpaRepository<PassengerRating, Integer> {

    @Query("""
            SELECT AVG(r.score)
            FROM PassengerRating r
            WHERE r.passengerId = :passengerId
            """)
    Optional<Float> getAverageRatingByPassengerId(@Param("passengerId") Integer passengerId);

    Page<PassengerRating> findAllByPassengerIdAndCommentIsNotNull(Integer driverId, Pageable pageable);
}
