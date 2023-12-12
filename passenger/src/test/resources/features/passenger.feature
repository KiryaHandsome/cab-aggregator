Feature: Retrieve passenger by id

  Scenario: Get request to /api/v1/passengers/{id} returns OK status
    Given Client provides id 1
    When Client makes request to "/api/v1/passengers/"
    Then Should return OK status and expected passenger

#Feature: Is it Friday yet?
#  Everybody wants to know when it's Friday
#
#  Scenario: Sunday isn't Friday
#    Given today is Sunday
#    When I ask whether it's Friday yet
#    Then I should be told "Nope"