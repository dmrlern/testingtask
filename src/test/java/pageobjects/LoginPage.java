package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import testprojectcore.core.Driver;
import testprojectcore.core.DriverUtils;
import testprojectcore.dataprovider.EnvironmentDataProvider;

import java.io.IOException;

public class LoginPage extends Driver {


    @FindBy(name = "login")
    WebElement usernameFormInput;

    @FindBy(name = "password")
    WebElement passwordFormInput;

    @FindBy(xpath = "//span[contains(.,'Login')]")
    WebElement submitButton;


    public LoginPage(WebDriver driver) {        //Initalize Web Elements using Selenium PageFactory
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void navigateToHomePage() throws IOException {
        driver.get(EnvironmentDataProvider.TESTDATA.getData("webapp" + "." + "baseURL"));
    }


    public void login(String userName, String password) {
        DriverUtils.waitUntil(ExpectedConditions.elementToBeClickable(usernameFormInput), 10, driver);
        usernameFormInput.sendKeys(userName);
        passwordFormInput.sendKeys(password);
        submitButton.click();
    }

}
