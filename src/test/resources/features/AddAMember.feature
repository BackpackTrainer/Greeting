
Feature: Add a new member and verify greeting

  As the system administrator,
  I want to add new members and their greetings to our club

  Business Rule:
  Member names must be unique.  Attempting to add a member with a name that
  already exists should result in an error message.

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

    Scenario:  Adding a new member with a duplicate name
      Given I have a browser open
      When I enter the url "localhost:3000"
      And I enter "Alice" and "Nice to meet you, Alice" in the Add Greeting form
      Then I should see the failure message "Failed to add greeting because the user already exists. To change the greeting for an existing member, use Update Greeting. Status 409"