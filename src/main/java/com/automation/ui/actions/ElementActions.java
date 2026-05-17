package com.automation.ui.actions;

import com.automation.core.driver.DriverManager;
import com.automation.core.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class ElementActions {

    public void click(By locator) {
        WaitUtils.clickable(locator).click();
    }

    public void type(By locator, String text) {
        WebElement element = WaitUtils.visible(locator);
        element.clear();
        element.sendKeys(text);
    }

    public String textOf(By locator) {
        return WaitUtils.visible(locator).getText();
    }

    public boolean isDisplayed(By locator) {
        return WaitUtils.visible(locator).isDisplayed();
    }

    public void scrollIntoView(By locator) {
        WebElement element = WaitUtils.visible(locator);
        ((JavascriptExecutor) DriverManager.getDriver())
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }
}