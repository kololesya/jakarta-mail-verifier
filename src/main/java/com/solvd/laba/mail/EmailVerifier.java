package com.solvd.laba.mail;

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

import com.solvd.laba.mail.config.EmailConfig;
import com.solvd.laba.mail.constants.MailConstants;
import com.solvd.laba.mail.constants.ProjectConstants;

import static com.solvd.laba.mail.constants.ProjectConstants.SECONDS_PER_MINUTE;

public class EmailVerifier {

    private final EmailConfig config;
    private final Duration searchWindow;
    private final Duration pollInterval;

    public EmailVerifier() throws IOException {
        this.config = new EmailConfig();
        this.searchWindow = Duration.ofMinutes(
                ProjectConstants.EMAIL_SEARCH_WINDOW_MINUTES
        );
        this.pollInterval = Duration.ofSeconds(10);
    }

    public boolean isEmailReceived(String subject, int windowMinutes) {
        Instant deadline = Instant.now().plusSeconds(windowMinutes * SECONDS_PER_MINUTE);
        while (Instant.now().isBefore(deadline)) {
            if (checkOnce(subject, windowMinutes)) {
                return true;
            }
            try {
                Thread.sleep(pollInterval.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
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
            return false;
        }
    }
}
