package com.automation.core.utils;

import com.automation.core.config.ConfigManager;
import com.automation.core.driver.DriverManager;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class WaitUtils {
    private WaitUtils() {
    }

    public static WebElement visible(By locator) {
        return webDriverWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement clickable(By locator) {
        return webDriverWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean titleContains(String title) {
        return webDriverWait().until(ExpectedConditions.titleContains(title));
    }

    private static WebDriverWait webDriverWait() {
        int timeout = ConfigManager.getInstance().getInt("explicit.wait.seconds");
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
    }
}