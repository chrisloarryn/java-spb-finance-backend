@regression @clients @clients_create
Feature: Client creation contract

  Background:
    * url baseUrl
    * configure headers = defaultHeaders

  Scenario: creating a client returns the persisted contract
    * def requestBody = buildCreateClientRequest()
    Given path 'api', 'clients'
    And request requestBody
    When method post
    Then status 201
    * match response contains clientResponseSchema
    * match response.name == requestBody.name
    * match response.clientId == requestBody.clientId
    * match response.identifier == requestBody.identifier
