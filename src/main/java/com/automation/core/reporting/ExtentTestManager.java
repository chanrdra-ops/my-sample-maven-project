package com.automation.core.reporting;

import com.aventstack.extentreports.ExtentTest;

public final class ExtentTestManager {
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();

    private ExtentTestManager() {
    }

    public static void createTest(String testName) {
        TEST.set(ExtentReportManager.getReport().createTest(testName));
    }

    public static ExtentTest getTest() {
        ExtentTest extentTest = TEST.get();
        if (extentTest == null) {
            throw new IllegalStateException("Extent test is not initialized for this thread.");
        }
        return extentTest;
    }

    public static void removeTest() {
        TEST.remove();
    }
}