
Feature: Retrieve all Members and Clear Displayed Member Names

  Background:
    Given I have a browser open
    And I enter the url "localhost:3000"

  Scenario: Greeting a  known user
    And I request get all members
    Then the following members should be returned:
      | name  | greeting                 |
      | Alice | Nice to meet you, Alice! |
      | Brett | Welcome, Brett!          |
      | Carol | Good evening, Carol!     |
      | Dante | Looking good, Dude!      |


Scenario:  Clear the member list
  And I request clear all members
  Then no members will be displayed
