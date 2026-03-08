@regression @movements @movements_create
Feature: Movement creation contract

  Background:
    * url baseUrl
    * configure headers = defaultHeaders

  Scenario: creating a deposit movement returns the persisted contract
    * def account = call read('classpath:karate/helpers/accounts/create-account.feature')
    * def requestBody = buildCreateMovementRequest({ accountNumber: account.accountNumber, transactionValue: 250.0 })
    Given path 'api', 'movements'
    And request requestBody
    When method post
    Then status 201
    * match response contains movementResponseSchema
    * match response.accountNumber == account.accountNumber
    * match response.detail contains 'Deposit'
