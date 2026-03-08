@regression @clients @clients_detail
Feature: Client detail contract

  Background:
    * url baseUrl
    * configure headers = defaultHeaders

  Scenario: fetching a client by id returns the expected payload
    * def created = call read('classpath:karate/helpers/clients/create-client.feature')
    Given path 'api', 'clients', created.clientId
    When method get
    Then status 200
    * match response contains clientResponseSchema
    * match response.id == created.clientId
    * match response.clientId == created.requestPayload.clientId
