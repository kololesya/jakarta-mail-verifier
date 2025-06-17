package com.solvd.mail.service;

import java.util.Properties;

import com.solvd.mail.config.EmailConfig;
import com.solvd.mail.constants.MailProtocol;

public class EmailPropertiesService {

    private final EmailConfig config;

    public EmailPropertiesService(EmailConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("EmailConfig must not be null");
        }
        this.config = config;
    }

    public Properties getStoreProperties() {
        Properties props = new Properties();
        MailProtocol protocol = MailProtocol.from(config.getProtocol());
        protocol.apply(props, config);
        return props;
    }
}
