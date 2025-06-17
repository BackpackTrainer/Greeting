Feature: Greeting

  Scenario: Greeting a  known user
    Given I have a browser open
    When I enter the url "localhost:3000"
    And I request get all members
    Then the following members should be returned:
      | name  | greeting                 |
      | Alice | Nice to meet you, Alice! |
      | Brett | Welcome, Brett!          |
      | Carol | Good evening, Carol!     |
      | Dante | Looking good, Dude!      |


Scenario:  Clear the member list
  Given I have a browser open
  When I enter the url "localhost:3000"
  And I request clear all members
  Then no members will be displayed
