package com.solvd.mail;

import java.io.IOException;

import com.solvd.mail.config.EmailConfig;
import com.solvd.mail.pages.SendEmailPage;
import com.solvd.mail.utils.ChromeCapabilitiesProvider;
import com.solvd.mail.verifier.EmailVerifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.solvd.mail.constants.ProjectConstants.*;

public class SendEmailPageTest {

    private static final Logger LOGGER = LogManager.getLogger(SendEmailPageTest.class);

    private WebDriver driver;
    private SendEmailPage page;
    private EmailVerifier verifier;
    private String recipient;

    @BeforeMethod
    public void setUp() throws IOException {
        EmailConfig emailConfig = new EmailConfig();
        recipient = emailConfig.getUsername();
        LOGGER.info("Testing send email to {}", recipient);
        ChromeOptions options = ChromeCapabilitiesProvider.getChromeCapabilities();
        driver = new ChromeDriver(options);
        page = new SendEmailPage(driver);
        verifier = new EmailVerifier();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(description = "Send test email via UI and verify reception")
    public void testSendAndReceiveEmail() {
        page.open();
        LOGGER.debug("Sending email to {} via UI", recipient);
        page.sendEmailTo(recipient);
        String successText = page.getSuccessMessage();
        Assert.assertEquals(successText, SUCCESS_MESSAGE, "Unexpected success message");
        LOGGER.info("Success message verified: {}", successText);
        String emailId = page.extractEmailId();
        String subject = EMAIL_SUBJECT_PREFIX + emailId;
        LOGGER.info("Checking email reception for subject: {}", subject);
        boolean received = verifier.isEmailReceived(subject, EMAIL_SEARCH_WINDOW_MINUTES);
        Assert.assertTrue(received,
                "The email with subject «" + subject + "» wasn't found in the inbox.");
    }
}
