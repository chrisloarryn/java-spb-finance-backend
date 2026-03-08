@regression @movements @movements_detail
Feature: Movement detail contract

  Background:
    * url baseUrl
    * configure headers = defaultHeaders

  Scenario: getting a movement by id returns the stored payload
    * def account = call read('classpath:karate/helpers/accounts/create-account.feature')
    * def requestBody = buildCreateMovementRequest({ accountNumber: account.accountNumber, transactionValue: 125.0 })
    Given path 'api', 'movements'
    And request requestBody
    When method post
    Then status 201
    * def movementId = response.id

    Given path 'api', 'movements', movementId
    When method get
    Then status 200
    * match response contains movementResponseSchema
    * match response.id == movementId
    * match response.accountNumber == account.accountNumber
