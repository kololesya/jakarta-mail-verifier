package com.solvd.mail.pages;

import java.time.Duration;
import java.util.NoSuchElementException;

import com.solvd.mail.verifier.EmailVerifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.solvd.mail.config.TestDataConfig;
import static com.solvd.mail.constants.ProjectConstants.CAPTCHA_WAIT;
import static com.solvd.mail.constants.ProjectConstants.DEFAULT_WAIT;

public class SendEmailPage extends BasePage {

    @FindBy(id = "ea2t")
    private WebElement emailField;

    @FindBy(id = "send_test_btn")
    private WebElement sendButton;

    @FindBy(className = "success")
    private WebElement successMessageValue;

    @FindBy(xpath = "//iframe[contains(@src,'recaptcha')]")
    private WebElement captchaFrame;

    @FindBy(css = ".recaptcha-checkbox-border")
    private WebElement captchaCheckbox;

    private static final Logger LOGGER = LogManager.getLogger(EmailVerifier.class);

    public SendEmailPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected String getPageUrl() {
        return TestDataConfig.get("url");
    }

    public void sendEmailTo(String address) {
        WebDriverWait wait = getWait();
        emailField.clear();
        emailField.sendKeys(address);
        attemptCaptcha(wait);
        wait.until(ExpectedConditions.elementToBeClickable(sendButton));
        sendButton.click();
    }

    public String getSuccessMessageFull() {
        WebDriverWait wait = getWait();
        wait.until(ExpectedConditions.visibilityOf(successMessageValue));
        return successMessageValue.getText().trim();
    }

    public String getSuccessMessage() {
        return getSuccessMessageFull().split("\n")[0].trim();
    }

    public String extractEmailId() {
        String full = getSuccessMessageFull();
        String[] parts = full.split("Email ID:");
        return parts.length < 2 ? null : parts[1].trim();
    }

    private void attemptCaptcha(WebDriverWait wait) {
        try {
            wait.withTimeout(Duration.ofSeconds(CAPTCHA_WAIT));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(captchaFrame));
            wait.until(ExpectedConditions.elementToBeClickable(captchaCheckbox)).click();
        } catch (NoSuchElementException | TimeoutException e) {
            LOGGER.debug("CAPTCHA handling skipped: {}", e.getMessage());
        } finally {
            getDriver().switchTo().defaultContent();
            wait.withTimeout(Duration.ofSeconds(DEFAULT_WAIT));
        }
    }

    private WebDriverWait getWait() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_WAIT));
    }
}
