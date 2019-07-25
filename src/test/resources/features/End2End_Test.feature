Feature: Automated End2End Tests
  Description: The purpose of this feature is to test End 2 End integration.

  Background:
    Given I am on Home Page
    When I search for product in dress category
    And I choose to buy the first item
    And I move to checkout from mini cart
  @wip
  Scenario: Customer place an order by purchasing an item from search
    And I enter my personal details as follows
      |  first_name  |last_name|     country        |     street_address     |          city     |postcode|phone_number|email_address|
      |TestAutomation| Opencast| United Kingdom (UK)|Hoults Yard, Walker Road|Newcastle upon Tyne|NE6 3PE |07438862327 |test@test.com|
    And I place the order
    Then Order details are successfully verified

  Scenario Outline: Customer place an order by purchasing an item from search - customer details are taken from JSON file
    And I enter <customer> personal details
    And I place the order
    Then Order details are successfully verified
    Examples:
      | customer |
      | Opencast |
      | Testuser |




