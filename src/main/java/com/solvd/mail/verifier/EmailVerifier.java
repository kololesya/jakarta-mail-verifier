package com.solvd.mail.verifier;

import java.io.IOException;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.awaitility.Awaitility.await;

import com.solvd.mail.config.EmailConfig;
import com.solvd.mail.service.EmailService;
import static com.solvd.mail.constants.ProjectConstants.POLL_INTERVAL_SECONDS;

public class EmailVerifier {

    private static final Logger LOGGER = LogManager.getLogger(EmailVerifier.class);
    private final EmailConfig config;
    private final EmailService emailService;
    private final Duration pollInterval;

    public EmailVerifier() throws IOException {
        this.config = new EmailConfig();
        this.emailService = new EmailService(config);
        this.pollInterval = Duration.ofSeconds(POLL_INTERVAL_SECONDS);
    }

    public boolean isEmailReceived(String subject, int windowMinutes) {
        Duration timeout = Duration.ofMinutes(windowMinutes);
        LOGGER.info("Waiting up to {} for email '{}'", timeout, subject);
        try {
            await()
                    .atMost(timeout)
                    .pollInterval(pollInterval)
                    .until(() -> emailService.isEmailPresent(subject, windowMinutes));
            LOGGER.info("Email '{}' received", subject);
            return true;
        } catch (Exception e) {
            LOGGER.warn("Did not receive email '{}' within {}", subject, timeout);
            return false;
        }
    }
}
