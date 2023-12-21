Feature: Passenger api

  Scenario: Get passenger by id
    Given passenger with id 1 exists
    When send request to get passenger with id 1
    Then response status code should be 200
    And should return john doe

  Scenario: Get passenger by not existing id
    Given passenger with id 10 should not exist
    When send request to get passenger with id 10
    Then response status code should be 404

  Scenario: Get all passengers
    When send request to get all passengers
    Then response status code should be 200

  Scenario: Delete passenger by id
    Given passenger with id 1 exists
    When send delete request with id 1
    Then response status code should be 204
    And passenger with id 1 should not exist

  Scenario Outline: Update passenger
    Given passenger with id 1 exists
    When send patch request for passenger with id 1 with email "<email>"
    Then response status code should be <response code>

    Examples:
      | email                   | response code |
      | newEmail@gmail.com      | 200           |
      | alice.smith@example.com | 409           |

  Scenario Outline: Create passenger
    Given request body where name "<name>" surname "<surname>" email "<email>" phone "<phone>"
    When send post request
    Then response status code should be <response code>

    Examples:
      | name  | surname  | email                   | phone         | response code |
      | Kiryl | Pryhozhy | newEmail@gmail.com      | +375441231231 | 201           |
      | Kiryl | Pryhozhy | alice.smith@example.com | +375445553333 | 409           |
      | K     | P        | invalidemail            | abc           | 400           |
