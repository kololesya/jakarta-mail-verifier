package com.solvd.laba.mail.pages;

import com.solvd.laba.mail.config.TestDataConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.NoSuchElementException;

import static com.solvd.laba.mail.constants.ProjectConstants.CAPTCHA_WAIT;
import static com.solvd.laba.mail.constants.ProjectConstants.DEFAULT_WAIT;

public class SendEmailPage extends BasePage {

    @FindBy(id = "ea2t")
    private WebElement emailField;

    @FindBy(id = "send_test_btn")
    private WebElement sendButton;

    @FindBy(className = "success")
    private WebElement successContainer;

    @FindBy(xpath = "//iframe[contains(@src,'recaptcha')]")
    private WebElement captchaFrame;

    @FindBy(css = ".recaptcha-checkbox-border")
    private WebElement captchaCheckbox;

    public SendEmailPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected String getPageUrl() {
        return TestDataConfig.get("url");
    }

    public void sendEmailTo(String address) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_WAIT));
        emailField.clear();
        emailField.sendKeys(address);
        try {
            wait.withTimeout(Duration.ofSeconds(CAPTCHA_WAIT));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(captchaFrame));
            wait.until(ExpectedConditions.elementToBeClickable(captchaCheckbox)).click();
            getDriver().switchTo().defaultContent();
        } catch (NoSuchElementException | org.openqa.selenium.TimeoutException e) {
            getDriver().switchTo().defaultContent();
        } finally {
            wait.withTimeout(Duration.ofSeconds(DEFAULT_WAIT));
        }
        wait.until(ExpectedConditions.elementToBeClickable(sendButton));
        sendButton.click();
    }

    public String getSuccessMessageFull() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_WAIT));
        wait.until(ExpectedConditions.visibilityOf(successContainer));
        return successContainer.getText().trim();
    }

    public String getSuccessMessage() {
        String full = getSuccessMessageFull();
        return full.split("\n")[0].trim();
    }

    public String extractEmailId() {
        String full = getSuccessMessageFull();
        String[] parts = full.split("Email ID:");
        if (parts.length < 2) {
            return null;
        }
        return parts[1].trim();
    }
}
