@api @all
Feature: API tests


  Scenario Outline: API /exercise_configs and /exercise_results tests

    * Call GET exercise configs with "<exercise id>"
    * GET exercise configs: It should return exercise info
    * POST exercise results "<exerciseConfigId>", "<sessionNumber>", "<startedAt>", "<finishedAt>"
    * POST exercise results: Http response status code should be 201 if not completed, 422 if already completed


    Examples:

      | exercise id | exerciseConfigId | sessionNumber | startedAt                | finishedAt               |
      | 275323947   | 275323945        | 275323945     | 2021-07-27T23:34:02.049Z | 2021-07-27T23:36:08.572Z |