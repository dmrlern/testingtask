@browser @all @steps
Feature: Steps

  Scenario Outline: Add steps, check if it changes the step counter


    * Log in to the UI with following credentials from the testdata file
      | username        | password        |
      | webapp.username | webapp.password |

    * Navigate to "Steps" section at the bottom right and click "ADD STEPS"
    * Add "<steps>" steps
    * Check if steps counter changed
    * Navigate to "My Week" page using the left Navigation Bar and click "GO TO STEPS" in "Steps" section on the bottom left


    Examples:
      | steps |
      | 150   |
      | 250   |