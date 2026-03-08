@regression @clients @clients_delete
Feature: Client deletion contract

  Background:
    * url baseUrl
    * configure headers = defaultHeaders

  Scenario: deleting a client removes it from subsequent lookups
    * def created = call read('classpath:karate/helpers/clients/create-client.feature')
    Given path 'api', 'clients', created.clientId
    When method delete
    Then status 204

    Given path 'api', 'clients', created.clientId
    When method get
    Then status 404
