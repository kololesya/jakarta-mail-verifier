package com.solvd.mail.service;

import com.solvd.mail.config.EmailConfig;
import com.solvd.mail.constants.MailConstants;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.search.AndTerm;
import jakarta.mail.search.ReceivedDateTerm;
import jakarta.mail.search.SearchTerm;
import jakarta.mail.search.SubjectTerm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;

import static com.solvd.mail.constants.ProjectConstants.SECONDS_PER_MINUTE;

public class EmailService {

    private static final Logger LOGGER = LogManager.getLogger(EmailService.class);
    private final EmailConfig config;
    private final EmailPropertiesService propertiesService;

    public EmailService(EmailConfig config) throws IOException {
        this.config = config;
        this.propertiesService = new EmailPropertiesService(config);
    }

    public boolean isEmailPresent(String subject, int windowMinutes) {
        Properties props = propertiesService.getStoreProperties();
        try (Store store = Session.getInstance(props).getStore(config.getProtocol())) {
            store.connect(config.getUsername(), config.getPassword());
            try (Folder inbox = store.getFolder(MailConstants.FOLDER_INBOX)) {
                inbox.open(Folder.READ_ONLY);
                SearchTerm criteria = buildSearchCriteria(subject, windowMinutes);
                Message[] results = inbox.search(criteria);
                LOGGER.debug("Search found {} messages", results.length);
                return results.length > 0;
            }
        } catch (MessagingException e) {
            LOGGER.error("Failed to search email: {}", e.getMessage(), e);
            return false;
        }
    }

    private SearchTerm buildSearchCriteria(String subject, int windowMinutes) {
        SubjectTerm subjectFilter = new SubjectTerm(subject);
        Date sinceDate = Date.from(Instant.now().minusSeconds(windowMinutes * SECONDS_PER_MINUTE));
        ReceivedDateTerm dateFilter = new ReceivedDateTerm(ReceivedDateTerm.GE, sinceDate);
        return new AndTerm(subjectFilter, dateFilter);
    }
}
