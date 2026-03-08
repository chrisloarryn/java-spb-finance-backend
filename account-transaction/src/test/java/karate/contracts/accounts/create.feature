@regression @accounts @accounts_create
Feature: Account creation contract

  Background:
    * url baseUrl
    * configure headers = defaultHeaders

  Scenario: creating an account returns the persisted contract
    * def requestBody = buildCreateAccountRequest()
    Given path 'api', 'accounts'
    And request requestBody
    When method post
    Then status 201
    * match response contains accountCreateResponseSchema
    * match response.accountNumber == requestBody.accountNumber
    * match response.clientId == clientId
