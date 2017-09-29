Feature: Status endpoint

  Scenario: Status endpoint returns OK
    When the status endpoint is called
    Then response is successful
    And response body contains 'OK'