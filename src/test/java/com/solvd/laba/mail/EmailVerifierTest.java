package com.solvd.laba.mail;

import org.testng.Assert;
import org.testng.annotations.Test;

import static com.solvd.laba.mail.ProjectConstants.*;

public class EmailVerifierTest {

    @Test
    public void testSendAndReceiveEmail() throws Exception {
        EmailConfig config = new EmailConfig();
        EmailSender sender = new EmailSender(config);
        String subject = TEST_SUBJECT_PREFIX + System.currentTimeMillis();
        sender.sendTestEmail(subject, TEST_EMAIL_BODY);
        Thread.sleep(EMAIL_DELIVERY_TIMEOUT_MS);
        EmailVerifier verifier = new EmailVerifier();
        boolean received = verifier.isEmailReceived(subject, EMAIL_SEARCH_WINDOW_MINUTES);
        Assert.assertTrue(received,
                "The email with subject «" + subject + "» doesn't found in the box.");
    }
}
