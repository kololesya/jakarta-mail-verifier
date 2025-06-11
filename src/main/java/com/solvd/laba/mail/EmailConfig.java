package com.solvd.laba.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.solvd.laba.mail.MailConstants.*;

public class EmailConfig {

    private final Properties properties;
    private final String protocol;
    private final String imapHost;
    private final int imapPort;
    private final String smtpHost;
    private final int smtpPort;
    private final boolean smtpAuth;
    private final boolean startTls;
    private final String username;
    private final String password;

    public EmailConfig() throws IOException {
        properties = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(MailConstants.PROP_FILE)) {
            if (in == null) {
                throw new IOException("email.properties not found in classpath");
            }
            properties.load(in);
        }
        protocol  = properties.getProperty(MailConstants.KEY_PROTOCOL);
        imapHost  = properties.getProperty(MailConstants.KEY_IMAP_HOST);
        imapPort  = Integer.parseInt(properties.getProperty(MailConstants.KEY_IMAP_PORT));
        smtpHost  = properties.getProperty(KEY_SMTP_HOST);
        smtpPort  = Integer.parseInt(properties.getProperty(KEY_SMTP_PORT));
        smtpAuth  = Boolean.parseBoolean(properties.getProperty(KEY_SMTP_AUTH));
        startTls  = Boolean.parseBoolean(properties.getProperty(KEY_SMTP_STARTTLS));
        username  = properties.getProperty(KEY_USERNAME);
        password  = properties.getProperty(KEY_PASSWORD);
    }

    public String getProtocol() {
        return protocol;
    }

    public String getImapHost() {
        return imapHost;
    }

    public int getImapPort() {
        return imapPort;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public boolean isSmtpAuth() {
        return smtpAuth;
    }

    public boolean isStartTls() {
        return startTls;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
