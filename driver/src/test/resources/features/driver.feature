Feature: Driver api

  Scenario: Get driver by id
    Given driver with id 1 exists
    When send request to get driver with id 1
    Then response status code should be 200
    And should return john doe

  Scenario: Get driver by not existing id
    Given driver with id 10 should not exist
    When send request to get driver with id 10
    Then response status code should be 404

  Scenario: Get all drivers
    When send request to get all drivers
    Then response status code should be 200

  Scenario: Delete driver by id
    Given driver with id 1 exists
    When send delete request with id 1
    Then response status code should be 204
    And driver with id 1 should not exist

  Scenario Outline: Update driver
    Given driver with id 1 exists
    When send patch request for driver with id 1 with email "<email>"
    Then response status code should be <response code>

    Examples:
      | email                  | response code |
      | newEmail@gmail.com     | 200           |
      | jane.smith@example.com | 409           |

  Scenario Outline: Create driver
    Given request body where name "<name>" surname "<surname>" email "<email>" phone "<phone>"
    When send post request
    Then response status code should be <response code>

    Examples:
      | name  | surname  | email                  | phone         | response code |
      | Kiryl | Pryhozhy | newEmail@gmail.com     | +375441231231 | 201           |
      | Kiryl | Pryhozhy | jane.smith@example.com | +375445553333 | 409           |
      | K     | P        | invalidemail           | abc           | 400           |
