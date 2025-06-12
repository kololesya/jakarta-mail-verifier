package com.solvd.laba.mail;

import com.solvd.laba.mail.pages.SendTestEmailPage;
import com.solvd.laba.mail.utils.ChromeCapabilitiesProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.solvd.laba.mail.constants.MailConstants.KEY_USERNAME;
import static com.solvd.laba.mail.constants.ProjectConstants.*;

public class SendTestEmailPageTest {

    @Test
    public void testOpenPage() throws IOException {
        ChromeOptions options = ChromeCapabilitiesProvider.getChromeCapabilities();
        WebDriver driver = new ChromeDriver(options);
        try {
            SendTestEmailPage page = new SendTestEmailPage(driver);
            page.open();
            page.sendEmailTo(KEY_USERNAME);
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
