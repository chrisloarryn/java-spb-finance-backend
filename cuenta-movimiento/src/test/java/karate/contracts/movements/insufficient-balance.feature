@regression @movements @movements_insufficient_balance
Feature: Insufficient balance contract

  Background:
    * url baseUrl
    * configure headers = defaultHeaders

  Scenario: withdrawing more than the available balance fails with 422
    * def account = call read('classpath:karate/helpers/accounts/create-account.feature') { saldoinicial: 100.0 }
    * def requestBody = buildCreateMovementRequest({ numerocuenta: account.accountNumber, valormovimiento: -250.0 })
    Given path 'api', 'movimientos'
    And request requestBody
    When method post
    Then status 422
    * match response.message contains 'suficiente saldo'
