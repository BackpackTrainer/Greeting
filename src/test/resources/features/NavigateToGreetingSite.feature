Feature:  Navigate to the home page
  Scenario:  Navigate to the site
    Given I have a browser open
    When I enter the url "localhost:3000"
    Then I am on the "React App" page