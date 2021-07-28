@browser @all @exercises

Feature: Exercises

  @exercises-sc1
  Scenario: Click on an exercise video and check if exercise info and the actual video is playing, complete the exercise

    * Log in to the UI with following credentials from the testdata file
      | username        | password        |
      | webapp.username | webapp.password |

    * Click "Start Training" in "My training" section on the left
    * In exercise info page, click "Watch" and check if video is playing
    * Click "START TRAINING"
    * After the exercise starts, check if the counter is counting and then wait for the exercise to complete


  @exercises-sc2
  Scenario: Skip an exercise, it shouldn't change the state of the completion dots

    * Log in to the UI with following credentials from the testdata file
      | username        | password        |
      | webapp.username | webapp.password |

    * Navigate to "My Week" page using the left Navigation Bar and click "GO TO EXERCISES" in "Exercises" section on the top left
    * Click a random exercise except the ones which are already completed
    * Click "START TRAINING"
    * After the exercise starts, quit the exercise
    * Check the counter dots to see they didn't change


  @exercises-sc3
  Scenario: Completion dots should change after completing an exercise

    * Log in to the UI with following credentials from the testdata file
      | username        | password        |
      | webapp.username | webapp.password |

    * Navigate to "My Week" page using the left Navigation Bar and click "GO TO EXERCISES" in "Exercises" section on the top left
    * Click a random exercise except the ones which are already completed
    * Click "START TRAINING"
    * After the exercise starts, check if the counter is counting and then wait for the exercise to complete
    * Check the counter dots to see they changed

  @exercises-sc4
  Scenario: Check if completed exercises have a check icon on them and click on a completed exercise

    * Log in to the UI with following credentials from the testdata file
      | username        | password        |
      | webapp.username | webapp.password |

    * Navigate to "My Week" page using the left Navigation Bar and click "GO TO EXERCISES" in "Exercises" section on the top left
    * Check if completed exercises have a check icon on them
    * Try to click a random exercise from those which are completed, it shouldn't allow that