package com.solvd.mail;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.solvd.mail.config.EmailConfig;
import com.solvd.mail.pages.SendEmailPage;
import com.solvd.mail.utils.ChromeCapabilitiesProvider;
import static com.solvd.mail.constants.ProjectConstants.*;

public class SendEmailPageTest {

    private static final Logger LOGGER = LogManager.getLogger(SendEmailPageTest.class);

    @Test
    public void testOpenPage() throws IOException {
        EmailConfig emailConfig = new EmailConfig();
        String recipient = emailConfig.getUsername();
        LOGGER.info("Testing send email to {}", recipient);
        ChromeOptions options = ChromeCapabilitiesProvider.getChromeCapabilities();
        WebDriver driver = new ChromeDriver(options);
        try {
            SendEmailPage page = new SendEmailPage(driver);
            page.open();
            LOGGER.debug("Sending email to {} via UI", recipient);
            page.sendEmailTo(recipient);
            Assert.assertEquals(page.getSuccessMessage(), SUCCESS_MESSAGE,
                    "The opened URL should match the configured testdata URL");
            LOGGER.info("Success message verified: {}", page.getSuccessMessage());
            String emailId = page.extractEmailId();
            EmailVerifier verifier = new EmailVerifier();
            String subject = EMAIL_SUBJECT_PREFIX + emailId;
            LOGGER.info("Checking email reception for subject: {}", subject);
            boolean received = verifier.isEmailReceived(subject, EMAIL_SEARCH_WINDOW_MINUTES);
            Assert.assertTrue(received,
                    "The email with subject «" + subject + "» doesn't found in the box.");
        } finally {
            driver.quit();
        }
    }
}
