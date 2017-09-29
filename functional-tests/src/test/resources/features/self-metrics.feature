Feature: Self metrics operations

  Background:
    Given database is empty

  Scenario: A new self metric is created
    Given a self metric object
    When the client 'post a self metric'
    Then response is successful