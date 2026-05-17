package com.automation.core.listeners;

import com.automation.core.driver.DriverManager;
import com.automation.core.reporting.ExtentReportManager;
import com.automation.core.reporting.ExtentTestManager;
import com.automation.core.utils.ScreenshotUtils;
import java.nio.file.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static final Logger LOGGER = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTestManager.createTest(result.getMethod().getMethodName());
        LOGGER.info("Starting test: {}", result.getMethod().getQualifiedName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTestManager.getTest().pass("Test passed");
        LOGGER.info("Test passed: {}", result.getMethod().getQualifiedName());
        ExtentTestManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTestManager.getTest().fail(result.getThrowable());
        LOGGER.error("Test failed: {}", result.getMethod().getQualifiedName(), result.getThrowable());

        if (DriverManager.hasDriver()) {
            Path screenshot = ScreenshotUtils.capture(result.getMethod().getMethodName());
            try {
                ExtentTestManager.getTest().addScreenCaptureFromPath(screenshot.toString());
            } catch (Exception exception) {
                LOGGER.warn("Unable to attach screenshot to report: {}", screenshot, exception);
            }
        }

        ExtentTestManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTestManager.getTest().skip(result.getThrowable());
        LOGGER.warn("Test skipped: {}", result.getMethod().getQualifiedName());
        ExtentTestManager.removeTest();
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flush();
    }
}