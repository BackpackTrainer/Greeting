@WhatImWorkingOnNow
Feature: Add a new member and verify greeting

  As the system administrator,
  I want to add new members and their greetings to our club

  Business Rule:
  Member names must be unique.  Attempting to add a member with a name that
  already exists should result in an error message.

  Background:
    Given I have a browser open
    When I enter the url "localhost:3000"


  Scenario Outline: Add a new member successfully
    And I enter "<name>" and "<greeting>" in the Add a New Member form
    Then I should see the success message "<successMessage>"
    When I search for "<name>" in the Find Greeting by Name form
    Then I should see "<greeting>" in the results

    Examples:
      | name   | greeting                              | successMessage                                                            |
      | Fred   | Fred, we even include dorks like you! | New member Fred added with message: Fred, we even include dorks like you!|
      | Marcie | Hello, Beautiful!                     | New member Marcie added with message: Hello, Beautiful!              |


    Scenario:  Adding a new member with a duplicate name
      And I enter "Alice" and "Nice to meet you, Alice" in the Add a New Member form
      Then I should see the failure message "Failed to add greeting because the user already exists. To change the greeting for an existing member, use Update Greeting. Status 409"


  Scenario: Submitting with an empty name should display validation error
    And I enter "" and "Some greeting" in the Add a New Member form
    Then I should see the "add" error message "Please enter all required information."
    And I capture a screenshot named "empty-name-validation"


  Scenario: Submitting with an empty greeting should display validation error
    And I enter "Fred" and "" in the Add a New Member form
    Then I should see the "add" error message "Please enter all required information."
    And I capture a screenshot named "empty-greeting-validation"


  Scenario: Submitting with empty name and an empty greeting should display validation errors
    And I enter "" and "" in the Add a New Member form
    Then I should see the "add" error message "Please enter all required information."
    And I capture a screenshot named "empty-name-and-greeting-validation"
