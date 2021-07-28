@browser @all @knowledgewellbeing
Feature: Knowledge & Wellbeing

  Scenario: Check Knowledge&Wellbeing videos, check if completion marks work

    * Log in to the UI with following credentials from the testdata file
      | username        | password        |
      | webapp.username | webapp.password |

    * Navigate to "My Week" page using the left Navigation Bar and click "VIEW CONTENT" in "Knowledge&Wellbeing" section on the top right
    * If there is any, click on an incomplete video and verify if it is marked with a check icon after the video is finished
    * If there are no incomplete video, click on a random video and check if it is playing