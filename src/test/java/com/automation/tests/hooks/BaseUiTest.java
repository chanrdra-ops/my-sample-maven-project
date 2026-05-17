package com.automation.tests.hooks;

import com.automation.core.driver.DriverFactory;
import com.automation.core.driver.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseUiTest {

    @BeforeMethod(alwaysRun = true)
    public void setUpUi() {
        DriverManager.setDriver(DriverFactory.createDriver());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownUi() {
        DriverManager.quitDriver();
    }
}