package com.automation.ui.pages;

import com.automation.core.driver.DriverManager;
import com.automation.core.utils.WaitUtils;
import com.automation.ui.actions.ElementActions;
import org.openqa.selenium.By;

public class SeleniumHomePage {
    private final ElementActions actions = new ElementActions();

    private final By downloadsLink = By.cssSelector("a[href*='downloads']");

    public SeleniumHomePage open(String baseUrl) {
        DriverManager.getDriver().get(baseUrl);
        WaitUtils.titleContains("Selenium");
        return this;
    }

    public boolean isLoaded() {
        return DriverManager.getDriver().getTitle().contains("Selenium");
    }

    public boolean isDownloadsLinkVisible() {
        return actions.isDisplayed(downloadsLink);
    }
}