package com.solvd.mail.constants;

import java.util.Properties;

import com.solvd.mail.config.EmailConfig;

public enum MailProtocol {
    IMAP("imap", "mail.imap.host", "mail.imap.port", "mail.imap.ssl.enable"),
    POP3("pop3", "mail.pop3.host", "mail.pop3.port", "mail.pop3.ssl.enable");

    private final String name;
    private final String hostKey;
    private final String portKey;
    private final String sslKey;

    MailProtocol(String name, String hostKey, String portKey, String sslKey) {
        this.name    = name;
        this.hostKey = hostKey;
        this.portKey = portKey;
        this.sslKey  = sslKey;
    }

    public String getName() { return name; }
    public String getHostKey() { return hostKey; }
    public String getPortKey() { return portKey; }
    public String getSslKey()  { return sslKey; }

    public void apply(Properties props, EmailConfig config) {
        props.put("mail.store.protocol", name);
        props.put(hostKey, config.getImapHost());
        props.put(portKey, String.valueOf(config.getImapPort()));
        props.put(sslKey, "true");
    }

    public static MailProtocol from(String protocolName) {
        for (MailProtocol p : values()) {
            if (p.name.equalsIgnoreCase(protocolName)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unsupported protocol: " + protocolName);
    }
}
