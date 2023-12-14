Feature: End ride

  Scenario: Driver ends ride successfully
    Given Driver with id 555 has status "BUSY"
    And There is ride with id "rideId" and driver id 555
    When End ride with ride id "rideId"
    Then Should change driver with id 555 status to "AVAILABLE"
    And Should update ride finish time

  Scenario: Driver try to end ended ride
    Given Driver with id 666 has status "AVAILABLE"
    And Ride with id "someId" already ended
    When End ride with waiting ride id "someId"
    Then Should throw ride already ended exception