package com.automation.core.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ExtentReportManager {
    private static ExtentReports extentReports;

    private ExtentReportManager() {
    }

    public static synchronized ExtentReports getReport() {
        if (extentReports == null) {
            createReport();
        }
        return extentReports;
    }

    private static void createReport() {
        Path reportPath = Path.of("target", "reports", "extent-report.html");
        try {
            Files.createDirectories(reportPath.getParent());
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to create report directory.", exception);
        }

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath.toString());
        sparkReporter.config().setDocumentTitle("Automation Execution Report");
        sparkReporter.config().setReportName("UI and API Automation");
        sparkReporter.config().setTheme(Theme.STANDARD);

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        extentReports.setSystemInfo("Framework", "Selenium Java TestNG Rest Assured");
    }

    public static synchronized void flush() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}