package com.automation.core.utils;

import com.automation.core.driver.DriverManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public final class ScreenshotUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {
    }

    public static Path capture(String testName) {
        if (!DriverManager.hasDriver()) {
            throw new IllegalStateException("Cannot capture screenshot because WebDriver is not initialized.");
        }

        File source = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
        Path target = Path.of("target", "screenshots", sanitize(testName) + "_" + FORMATTER.format(LocalDateTime.now()) + ".png");

        try {

            Files.createDirectories(target.getParent());
            return Files.copy(source.toPath(), target);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to save screenshot: " + target, exception);
        }
    }

    private static String sanitize(String value) {
        return value.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}