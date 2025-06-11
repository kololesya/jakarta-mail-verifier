package com.solvd.laba.mail;

public class ProjectConstants {

    private ProjectConstants() {
    }

    public static final long EMAIL_DELIVERY_TIMEOUT_MS = 30_000L;

    public static final int EMAIL_SEARCH_WINDOW_MINUTES = 5;

    public static final long SECONDS_PER_MINUTE = 60L;

    public static final String TEST_EMAIL_BODY = "Automated test email.";

    public static final String TEST_SUBJECT_PREFIX = "TestSubject-";
}
