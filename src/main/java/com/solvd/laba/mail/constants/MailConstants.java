package com.solvd.laba.mail.constants;

public final class MailConstants {

    private MailConstants() {}

    public static final String PROP_FILE = "email.properties";

    public static final String KEY_PROTOCOL = "mail.server.protocol";
    public static final String KEY_IMAP_HOST = "mail.server.host";
    public static final String KEY_IMAP_PORT = "mail.server.port";
    public static final String KEY_SMTP_HOST = "mail.smtp.host";
    public static final String KEY_SMTP_PORT = "mail.smtp.port";
    public static final String KEY_SMTP_AUTH = "mail.smtp.auth";
    public static final String KEY_SMTP_STARTTLS = "mail.smtp.starttls.enable";
    public static final String KEY_USERNAME = "mail.username";
    public static final String KEY_PASSWORD = "mail.password";

    public static final String STORE_PROTOCOL = "mail.store.protocol";
    public static final String SSL_ENABLE_FMT = "mail.%s.ssl.enable";
    public static final String FOLDER_INBOX = "INBOX";
}
