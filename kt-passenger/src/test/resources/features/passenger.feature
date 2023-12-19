Feature: Retrieve passenger by id

  Scenario: Get request to /api/v1/passengers/{id} returns OK status
    Given Client provides id 1
    When Client makes request to "/api/v1/passengers/"
    Then Should return OK status and expected passenger