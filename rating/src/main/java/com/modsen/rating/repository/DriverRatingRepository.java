package com.modsen.rating.repository;

import com.modsen.rating.model.DriverRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DriverRatingRepository extends JpaRepository<DriverRating, Integer> {

    @Query("""
            SELECT AVG(r.score)
            FROM DriverRating r
            WHERE r.driverId = :driverId
            """)
    Optional<Float> getAverageRatingByDriverId(@Param("driverId") Integer driverId);

    Page<DriverRating> getAllByDriverIdAndCommentIsNotNull(Integer driverId, Pageable pageable);
}
