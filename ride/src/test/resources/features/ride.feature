Feature: Start ride

  Scenario: Driver starts ride successfully
    Given Driver with id 123 has status "AVAILABLE"
    And There is waitingRide with id "someId"
    When Start ride with waitingRideId "someId" and driverId 123
    Then Should change driver with id 123 status to "BUSY"
    And Should remove waiting ride with id "someId"