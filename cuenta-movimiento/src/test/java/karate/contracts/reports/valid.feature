@regression @reports @reports_valid
Feature: Valid report contract

  Background:
    * url baseUrl
    * configure headers = defaultHeaders

  Scenario: report returns movements for the requested client and date
    * def account = call read('classpath:karate/helpers/accounts/create-account.feature')
    * def movementRequest = buildCreateMovementRequest({ numerocuenta: account.accountNumber, valormovimiento: 300.0 })
    Given path 'api', 'movimientos'
    And request movementRequest
    When method post
    Then status 201

    Given path 'api', 'reports'
    And param fecha = today
    And param cliente = clientId
    When method get
    Then status 200
    * match response contains reportResponseSchema
    * assert response.resultados >= 1
    * def selected = karate.filter(response.data, function(item){ return item.numerocuenta == account.accountNumber })
    * match selected == '#[1]'
    * match selected[0] contains movementResponseSchema
