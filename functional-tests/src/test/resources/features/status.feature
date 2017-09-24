Feature: Status endpoint

  Scenario: Status endpoint returns OK
    When the status endpoint is called
    Then response is successful and body is 'OK'