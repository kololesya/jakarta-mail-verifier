package com.solvd.laba.mail;

import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.search.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;

import static com.solvd.laba.mail.ProjectConstants.SECONDS_PER_MINUTE;

public class EmailVerifier {

    private final EmailConfig config;

    public EmailVerifier() throws IOException {
        this.config = new EmailConfig();
    }

    public boolean isEmailReceived(String subject, int minutes) {
        String protocol = config.getProtocol();
        Properties props = new Properties();
        props.put(MailConstants.STORE_PROTOCOL, protocol);
        props.put("mail." + protocol + ".host", config.getImapHost());
        props.put("mail." + protocol + ".port", Integer.toString(config.getImapPort()));
        String sslKey = String.format(MailConstants.SSL_ENABLE_FMT, protocol);
        props.put(sslKey, "true");

        try {
            Session session = Session.getInstance(props);
            Store store = session.getStore(protocol);
            store.connect(config.getUsername(), config.getPassword());
            Folder inbox = store.getFolder(MailConstants.FOLDER_INBOX);
            inbox.open(Folder.READ_ONLY);
            SubjectTerm subjectTerm = new SubjectTerm(subject);
            Date since = Date.from(Instant.now().minusSeconds(minutes * SECONDS_PER_MINUTE));
            ReceivedDateTerm dateTerm = new ReceivedDateTerm(ComparisonTerm.GE, since);
            SearchTerm searchTerm = new AndTerm(subjectTerm, dateTerm);
            Message[] found = inbox.search(searchTerm);
            inbox.close(false);
            store.close();
            return found != null && found.length > 0;
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to verify email", e);
        }
    }
}
