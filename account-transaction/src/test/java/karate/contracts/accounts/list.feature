@regression @accounts @accounts_list
Feature: Account listing contract

  Background:
    * url baseUrl
    * configure headers = defaultHeaders

  Scenario: listing accounts includes newly created rows
    * def created = call read('classpath:karate/helpers/accounts/create-account.feature')
    Given path 'api', 'accounts'
    When method get
    Then status 200
    * match response == '#[]'
    * def selected = karate.filter(response, function(item){ return item.id == created.accountId })
    * match selected == '#[1]'
    * match selected[0] contains accountResponseSchema
    * match selected[0].accountNumber == created.accountNumber
