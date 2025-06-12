package com.solvd.laba.mail.constants;

import java.time.Duration;

public class ProjectConstants {

    private ProjectConstants() {
    }

    public static final long EMAIL_DELIVERY_TIMEOUT_MS = 30_000L;

    public static final int EMAIL_SEARCH_WINDOW_MINUTES = 5;

    public static final long SECONDS_PER_MINUTE = 60L;

    public static final String EMAIL_SUBJECT_PREFIX = "SendTestEmail.com - Testing Email ID: ";

    public static final String SUCCESS_MESSAGE = "Success - A test message has been sent!";

    public static final long DEFAULT_WAIT = 15L;

    public static final long CAPTCHA_WAIT = 5L;
}
