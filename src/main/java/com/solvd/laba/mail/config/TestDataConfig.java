package com.solvd.laba.mail.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestDataConfig {

    private static final String FILE_NAME = "testdata.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream in =
                     TestDataConfig.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (in == null) {
                throw new IllegalStateException(FILE_NAME + " not found");
            }
            PROPERTIES.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private TestDataConfig() { }

    public static String get(String key) {
        String val = PROPERTIES.getProperty(key);
        if (val == null) {
            throw new IllegalArgumentException("Key '" + key + "' not found in " + FILE_NAME);
        }
        return val;
    }
}
