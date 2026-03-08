@ignore
Feature: Create a client and expose its identifier

  Background:
    * url baseUrl
    * configure headers = defaultHeaders

  Scenario:
    * def requestBody = buildCreateClientRequest(__arg)
    Given path 'api', 'clients'
    And request requestBody
    When method post
    Then status 201
    * match response contains clientResponseSchema
    * def client = response
    * def clientId = response.id
    * def requestPayload = requestBody
