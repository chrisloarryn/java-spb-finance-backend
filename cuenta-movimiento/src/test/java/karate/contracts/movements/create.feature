@regression @movements @movements_create
Feature: Movement creation contract

  Background:
    * url baseUrl
    * configure headers = defaultHeaders

  Scenario: creating a deposit movement returns the persisted contract
    * def account = call read('classpath:karate/helpers/accounts/create-account.feature')
    * def requestBody = buildCreateMovementRequest({ numerocuenta: account.accountNumber, valormovimiento: 250.0 })
    Given path 'api', 'movimientos'
    And request requestBody
    When method post
    Then status 201
    * match response contains movementResponseSchema
    * match response.numerocuenta == account.accountNumber
    * match response.detalle contains 'Deposito'
