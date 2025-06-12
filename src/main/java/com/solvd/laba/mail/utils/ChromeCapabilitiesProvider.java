package com.solvd.laba.mail.utils;

import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class ChromeCapabilitiesProvider {

    private ChromeCapabilitiesProvider() {
    }

    public static ChromeOptions getChromeCapabilities() throws IOException {

        ChromeOptions options = new ChromeOptions();
        Path tempProfile = Files.createTempDirectory("chrome-profile-");
        options.addArguments(
                "--user-data-dir=" + tempProfile.toAbsolutePath(),
                "--incognito",
                "--start-maximized",
                "--disable-notifications",
                "--disable-popup-blocking",
                "--disable-blink-features=PasswordLeakDetection",
                "--disable-features=PasswordLeakDetection,PasswordManagerLeakDetection",
                "--disable-save-password-bubble",
                "--disable-password-generation"
        );
        Map<String, Object> prefs = Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "password_manager_enabled", false,
                "profile.password_manager_leak_detection_enabled", false,
                "profile.default_content_setting_values.notifications", 2
        );
        options.setExperimentalOption("prefs", prefs);

        return options;
    }
}
