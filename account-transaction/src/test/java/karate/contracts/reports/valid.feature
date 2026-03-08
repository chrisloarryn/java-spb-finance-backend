@regression @reports @reports_valid
Feature: Valid report contract

  Background:
    * url baseUrl
    * configure headers = defaultHeaders

  Scenario: report returns movements for the requested client and date
    * def account = call read('classpath:karate/helpers/accounts/create-account.feature')
    * def movementRequest = buildCreateMovementRequest({ accountNumber: account.accountNumber, transactionValue: 300.0 })
    Given path 'api', 'movements'
    And request movementRequest
    When method post
    Then status 201

    Given path 'api', 'reports'
    And param date = today
    And param clientId = clientId
    When method get
    Then status 200
    * match response contains reportResponseSchema
    * assert response.results >= 1
    * def selected = karate.filter(response.data, function(item){ return item.accountNumber == account.accountNumber })
    * match selected == '#[1]'
    * match selected[0] contains movementResponseSchema
