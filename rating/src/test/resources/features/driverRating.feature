Feature: Driver rating api

  Scenario: Add valid score to driver
    Given score request with score 5 and comment "Good!"
    When add score to driver with id 1
    Then response status should be 201
    And there should be new driver rating with id 4

  Scenario: Add invalid score to driver
    Given score request with score 10 and comment "So Good!"
    When add score to driver with id 1
    Then response status should be 400

  Scenario: Get driver average rating
    When get average rating of driver with id 1
    Then rating should be close to 3.0
    And response status should be 200