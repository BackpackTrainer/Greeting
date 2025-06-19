@WhatImWorkingOnNow
Feature: Add a new member and verify greeting

  In order to add new members to the system
  As a user
  I want to submit new names and greetings and verify they were successfully stored

  Scenario Outline: Add a new member successfully
    Given I have a browser open
    When I enter the url "localhost:3000"
    And I enter "<name>" and "<greeting>" in the Add Greeting form
    Then I should see the success message "<successMessage>"
    When I search for "<name>" in the Find Greeting by Name form
    Then I should see "<greeting>" in the results

    Examples:
      | name   | greeting                              | successMessage                                                            |
      | Fred   | Fred, we even include dorks like you! | New member Fred added with message: Fred, we even include dorks like you! 🎉 |
      | Marcie | Hello, Beautiful!                     | New member Marcie added with message: Hello, Beautiful! 🎉                 |
