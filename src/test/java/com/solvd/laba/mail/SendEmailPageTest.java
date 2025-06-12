package com.solvd.laba.mail;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.solvd.laba.mail.config.EmailConfig;
import com.solvd.laba.mail.pages.SendEmailPage;
import com.solvd.laba.mail.utils.ChromeCapabilitiesProvider;
import static com.solvd.laba.mail.constants.ProjectConstants.*;

public class SendEmailPageTest {

    @Test
    public void testOpenPage() throws IOException {
        EmailConfig emailConfig = new EmailConfig();
        String recipient = emailConfig.getUsername();
        ChromeOptions options = ChromeCapabilitiesProvider.getChromeCapabilities();
        WebDriver driver = new ChromeDriver(options);
        try {
            SendEmailPage page = new SendEmailPage(driver);
            page.open();
            page.sendEmailTo(recipient);
            Assert.assertEquals(page.getSuccessMessage(), SUCCESS_MESSAGE,
                    "The opened URL should match the configured testdata URL");
            String emailId = page.extractEmailId();
            EmailVerifier verifier = new EmailVerifier();
            String subject = EMAIL_SUBJECT_PREFIX + emailId;
            boolean received = verifier.isEmailReceived(subject, EMAIL_SEARCH_WINDOW_MINUTES);
            Assert.assertTrue(received,
                    "The email with subject «" + subject + "» doesn't found in the box.");
        } finally {
            driver.quit();
        }
    }
}
