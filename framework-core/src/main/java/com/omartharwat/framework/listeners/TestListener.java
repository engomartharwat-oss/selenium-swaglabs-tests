package com.omartharwat.framework.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.omartharwat.framework.driver.DriverManager;
import com.omartharwat.framework.reporting.ExtentManager;
import com.omartharwat.framework.utils.ScreenshotUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Path;
import java.util.Arrays;

/**
 * TestNG listener. It writes every test into the Extent HTML report
 * and takes a screenshot automatically when a test fails.
 */
public class TestListener implements ITestListener {

    private static final ThreadLocal<ExtentTest> CURRENT = new ThreadLocal<>();
    private final ExtentReports extent = ExtentManager.getInstance();

    @Override
    public void onTestStart(ITestResult result) {
        String name = result.getMethod().getMethodName();
        Object[] params = result.getParameters();
        if (params != null && params.length > 0) {
            name = name + " " + Arrays.toString(params);
        }
        ExtentTest test = extent.createTest(name, result.getMethod().getDescription());
        test.assignCategory(result.getTestClass().getRealClass().getSimpleName());
        CURRENT.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        CURRENT.get().pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = CURRENT.get();
        test.fail(result.getThrowable());
        if (DriverManager.hasDriver()) {
            Path shot = ScreenshotUtils.capture(DriverManager.getDriver(), result.getMethod().getMethodName());
            test.info("Screenshot saved: " + shot.toAbsolutePath());
            try {
                test.addScreenCaptureFromPath(shot.toAbsolutePath().toString());
            } catch (Exception ignored) {
                // the screenshot file is still on disk
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        CURRENT.get().skip("Test skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
