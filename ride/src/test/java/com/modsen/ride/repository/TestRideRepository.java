package com.modsen.ride.repository;

import com.modsen.ride.model.WaitingRide;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@Repository
public class TestRideRepository {

    private final MongoTemplate mongoTemplate;

    public TestRideRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Optional<WaitingRide> findWaitingRideByPassengerId(Integer passengerId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("passengerId").is(passengerId));
        List<WaitingRide> rides = mongoTemplate.find(query, WaitingRide.class);
        if (rides.isEmpty())
            return Optional.empty();
        return Optional.of(rides.get(0));
    }


}
