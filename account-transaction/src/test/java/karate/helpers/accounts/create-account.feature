@ignore
Feature: Create an account and expose its identifier

  Background:
    * url baseUrl
    * configure headers = defaultHeaders

  Scenario:
    * def requestBody = buildCreateAccountRequest(__arg)
    Given path 'api', 'accounts'
    And request requestBody
    When method post
    Then status 201
    * match response contains accountCreateResponseSchema
    * def account = response
    * def accountId = response.id
    * def accountNumber = response.accountNumber
    * def requestPayload = requestBody
