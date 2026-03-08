@regression @clients @clients_validation
Feature: Client validation contract

  Background:
    * url baseUrl
    * configure headers = defaultHeaders

  Scenario: creating a client with a short name fails validation
    * def requestBody = buildCreateClientRequest({ nombre: 'ab' })
    Given path 'api', 'clients'
    And request requestBody
    When method post
    Then status 400
    * match response.status == 400
