@browser @all @checkcounts
Feature: Check counts

  Scenario: Check all counters

    * Log in to the UI with following credentials from the testdata file
      | username        | password        |
      | webapp.username | webapp.password |

    * Navigate to "My Week" page using the left Navigation Bar and click "GO TO EXERCISES" in "Exercises" section on the top left
    * Get the incomplete exercise repetitions count and then return back to "My Week" page to see if the counter is right
    * Navigate to "My Week" page using the left Navigation Bar and click "VIEW CONTENT" in "Knowledge&Wellbeing" section on the top right
    * Get the incomplete knowledge video count and then return back to "My Week" page to see if the counter is right
