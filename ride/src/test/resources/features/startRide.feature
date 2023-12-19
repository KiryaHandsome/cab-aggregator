Feature: Start ride

  Scenario: Driver starts ride successfully
    Given Driver with id 123 has status "AVAILABLE"
    And There is waiting ride with id "someId"
    When Start ride with waiting ride id "someId" and driver id 123
    Then Should change driver with id 123 status to "BUSY"
    And Should remove waiting ride with id "someId"

  Scenario: Driver try to start started ride
    Given Driver with id 111 has status "AVAILABLE"
    And There is no waiting ride with id "someId"
    When Start ride with waiting ride id "someId" and driver id 111
    Then Should throw waiting ride not found exception