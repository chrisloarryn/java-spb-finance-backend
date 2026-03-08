@regression @clients @clients_update
Feature: Client update contract

  Background:
    * url baseUrl
    * configure headers = defaultHeaders

  Scenario: updating a client returns the modified contract
    * def created = call read('classpath:karate/helpers/clients/create-client.feature')
    * def requestBody = buildUpdateClientRequest()
    Given path 'api', 'clients', created.clientId
    And request requestBody
    When method put
    Then status 200
    * match response contains clientResponseSchema
    * match response.id == created.clientId
    * match response.name == requestBody.name
    * match response.clientId == requestBody.clientId
