package com.omartharwat.framework.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

/** Creates the single ExtentReports object that writes target/extent-report.html. */
public final class ExtentManager {

    private static ExtentReports extent;

    private ExtentManager() {
    }

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("target/extent-report.html");
            spark.config().setDocumentTitle("Swag Labs Test Report");
            spark.config().setReportName("Swag Labs UI Automation");
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Java", System.getProperty("java.version"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
        }
        return extent;
    }
}
