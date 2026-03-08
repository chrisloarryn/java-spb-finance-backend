@regression @reports @reports_invalid_date
Feature: Invalid date report contract

  Background:
    * url baseUrl
    * configure headers = defaultHeaders

  Scenario: reports reject dates that do not follow yyyy-MM-dd
    Given path 'api', 'reports'
    And param fecha = '2026/03/08'
    And param cliente = clientId
    When method get
    Then status 400
    * match response.message contains 'formato'
