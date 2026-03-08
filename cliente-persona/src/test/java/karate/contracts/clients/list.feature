@regression @clients @clients_list
Feature: Client listing contract

  Background:
    * url baseUrl
    * configure headers = defaultHeaders

  Scenario: listing clients includes created records
    * def created = call read('classpath:karate/helpers/clients/create-client.feature')
    Given path 'api', 'clients'
    When method get
    Then status 200
    * match response == '#[]'
    * def selected = karate.filter(response, function(item){ return item.id == created.clientId })
    * match selected == '#[1]'
    * match selected[0] contains clientResponseSchema
    * match selected[0].nombre == created.requestPayload.nombre
