@WhatImWorkingOnNow
Feature: Updating greetings for members

  Background:
    Given I have a browser open
    And I enter the url "localhost:3000"

  Scenario Outline: Update the greeting for an existing member successfully
    Given I have a browser open
    When I enter the url "localhost:3000"
    And I enter "<name>" and "<greeting>" in the Updating Greeting form
    Then I should see the greeting successfully updated message "<successMessage>"
    When I search for "<name>" in the Find Greeting by Name form
    Then I should see the greeting "<greeting>" for "<name>"

    Examples:
      | name  | greeting              | successMessage                               |
      | Alice | Welcome, Alice        | Greeting for Alice was successfully updated. |
      | Brett | Dude! Nice to see you | Greeting for Brett was successfully updated. |

  Scenario:  Updating the greeting for a non-existing member fails
    Given I have a browser open
    When I enter the url "localhost:3000"
    And I enter "Tarzan" and "Me Tarzan" in the Updating Greeting form
    Then I should see the greeting failed to update message "No update made: Member \"Tarzan\" was not found in the database."
