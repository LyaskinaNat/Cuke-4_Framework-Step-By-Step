Feature: Automated End2End Tests
  Description: The purpose of this feature is to test End 2 End integration.

  Scenario: Customer place an order by purchasing an item from search
    Given I am on Home Page
    When I search for product in dress category
    And I choose to buy the first item
    And I move to checkout from mini cart
    And I enter my personal details
    And I place the order
    Then Order details are successfully verified


