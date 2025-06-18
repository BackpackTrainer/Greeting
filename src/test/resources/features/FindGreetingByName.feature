Feature:  Find Greeting By Name
  In order to find a greeting by name
  As a user
  I want to be able to search for greetings by their name

  Scenario Outline: Find greeting by name for existing member
    Given I have a browser open
    When I enter the url "localhost:3000"
    And I enter "<name>" in the Find Greeting by name search field
    Then I should see "<greeting>" in the results

    Examples:
      | name  | greeting                 |
      | Alice | Nice to meet you, Alice! |
      | Brett | Welcome, Brett!          |
      | Carol | Good evening, Carol!     |
      | Dante | Looking good, Dude!      |

  Scenario: Find greeting by name for non-existing member
    Given I have a browser open
    When I enter the url "localhost:3000"
    And I enter "Tarzan" in the Find Greeting by name search field
    Then I should see this error message "Tarzan is not currently a member. Use the Add a New Member function if you would like to add them." in the results