package com.solvd.laba.mail;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class EmailSender {

    private static final Logger LOGGER = LogManager.getLogger(EmailSender.class);
    private static final String SMTP_HOST_KEY = "mail.smtp.host";
    private static final String SMTP_PORT_KEY = "mail.smtp.port";
    private static final String SMTP_AUTH_KEY = "mail.smtp.auth";
    private static final String SMTP_STARTTLS_KEY = "mail.smtp.starttls.enable";

    private final EmailConfig config;

    public EmailSender(EmailConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("EmailConfig must not be null");
        }
        this.config = config;
    }

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        validateParameters(to, subject, body);
        Properties smtpProps = buildSmtpProperties();

        Session session = Session.getInstance(smtpProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        config.getUsername(), config.getPassword()
                );
            }
        });
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(config.getUsername()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to, false)
            );
            message.setSubject(subject);
            message.setText(body);
            LOGGER.info("Sending email to {} with subject={}", to, subject);
            Transport.send(message);
            LOGGER.info("Email sent successfully to {}", to);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send email to {}: {}", to, e.getMessage());
            throw e;
        }
    }

    public void sendTestEmail(String subject, String body) throws MessagingException {
        sendEmail(config.getUsername(), subject, body);
    }

    private Properties buildSmtpProperties() {
        Properties props = new Properties();
        props.put(SMTP_HOST_KEY, config.getSmtpHost());
        props.put(SMTP_PORT_KEY, String.valueOf(config.getSmtpPort()));
        props.put(SMTP_AUTH_KEY, String.valueOf(config.isSmtpAuth()));
        props.put(SMTP_STARTTLS_KEY, String.valueOf(config.isStartTls()));
        return props;
    }

    private void validateParameters(String to, String subject, String body) {
        if (to == null || to.isBlank()) {
            throw new IllegalArgumentException("Recipient address must not be empty");
        }
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Subject must not be empty");
        }
        if (body == null) {
            throw new IllegalArgumentException("Email body must not be null");
        }
    }
}
