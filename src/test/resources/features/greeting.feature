Feature: Greeting

  Scenario: Greeting a user
    Given a user named "Alice"
    When the user is greeted
    Then the message should be "Hello, Alice!"

  Scenario: Greeting a user
    Given a user named "Carol"
    When the user is greeted
    Then the message should be "Hello, Carol!"