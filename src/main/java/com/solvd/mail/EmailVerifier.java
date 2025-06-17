package com.solvd.mail;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;

import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.search.ReceivedDateTerm;
import jakarta.mail.search.SearchTerm;
import jakarta.mail.search.SubjectTerm;

import com.solvd.mail.config.EmailConfig;
import com.solvd.mail.constants.MailConstants;
import com.solvd.mail.constants.ProjectConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.solvd.mail.constants.ProjectConstants.SECONDS_PER_MINUTE;

public class EmailVerifier {

    private static final Logger LOGGER = LogManager.getLogger(EmailVerifier.class);
    private final EmailConfig config;
    private final Duration pollInterval;

    public EmailVerifier() throws IOException {
        this.config = new EmailConfig();
        this.pollInterval = Duration.ofSeconds(ProjectConstants.POLL_INTERVAL_SECONDS);
    }

    public boolean isEmailReceived(String subject, int windowMinutes) {
        Instant deadline = Instant.now().plusSeconds(windowMinutes * SECONDS_PER_MINUTE);
        LOGGER.info("Start polling for email with subject '{}' until {}", subject, deadline);
        while (Instant.now().isBefore(deadline)) {
            LOGGER.debug("Checking inbox for subject '{}' at {}", subject, Instant.now());
            if (checkOnce(subject, windowMinutes)) {
                LOGGER.info("Email with subject '{}' found", subject);
                return true;
            }
            try {
                Thread.sleep(pollInterval.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.warn("Polling interrupted", e);
                break;
            }
        }
        LOGGER.warn("Deadline reached. Email with subject '{}' not found", subject);
        return false;
    }

    private boolean checkOnce(String subject, int minutes) {
        Properties props = new Properties();
        props.put(MailConstants.STORE_PROTOCOL, config.getProtocol());
        props.put("mail." + config.getProtocol() + ".host", config.getImapHost());
        props.put("mail." + config.getProtocol() + ".port", String.valueOf(config.getImapPort()));
        props.put(String.format(MailConstants.SSL_ENABLE_FMT, config.getProtocol()), "true");

        try (Store store = Session.getInstance(props)
                .getStore(config.getProtocol())) {
            store.connect(config.getUsername(), config.getPassword());
            try (Folder inbox = store.getFolder(MailConstants.FOLDER_INBOX)) {
                inbox.open(Folder.READ_ONLY);
                SubjectTerm subjTerm = new SubjectTerm(subject);
                Date sinceDate = Date.from(Instant.now().minusSeconds(minutes * SECONDS_PER_MINUTE));
                SearchTerm dateTerm = new ReceivedDateTerm(ReceivedDateTerm.GE, sinceDate);
                Message[] found = inbox.search(new jakarta.mail.search.AndTerm(subjTerm, dateTerm));
                return found != null && found.length > 0;
            }
        } catch (MessagingException e) {
            LOGGER.error("Error during email lookup", e);
            return false;
        }
    }
}
