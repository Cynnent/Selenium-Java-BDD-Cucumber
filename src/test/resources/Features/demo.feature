
Feature: Validate Login functionalities
  I want to use this template for my feature file

	@demo
  Scenario Outline: Valid Login
    Given I open the url "https://Stg.xxxxx.com"
    When I set "xxxxx@cynnent.com" to the inputfield "loc_txt_username"
    When I set "xxxxxxx" to the inputfield "loc_txt_password"
    When I click on the button "loc_btn_login"
    Then I expect that element "loc_toast_message" matches the text "SUCCESS!\nUser Login Successfful"    