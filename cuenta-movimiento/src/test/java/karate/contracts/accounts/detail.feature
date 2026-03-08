@regression @accounts @accounts_detail
Feature: Account detail contract

  Background:
    * url baseUrl
    * configure headers = defaultHeaders

  Scenario: getting an account by id returns the stored payload
    * def created = call read('classpath:karate/helpers/accounts/create-account.feature')
    Given path 'api', 'cuentas', created.accountId
    When method get
    Then status 200
    * match response contains accountResponseSchema
    * match response.id == created.accountId
    * match response.numero == created.accountNumber
