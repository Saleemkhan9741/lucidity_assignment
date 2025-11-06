package com.lucidity.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestListener implements ITestListener {

    private static final ExtentReports extent = ReportManager.getInstance();

    // Parent tests for each method
    private static final Map<String, ExtentTest> parentTests = new ConcurrentHashMap<>();

    // Thread-safe child test holder
    private static final ThreadLocal<ExtentTest> childTest = new ThreadLocal<>();

    @Override
    public synchronized void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();

        // Parent test (once per @Test method)
        ExtentTest parent = parentTests.computeIfAbsent(methodName, k -> {
            ExtentTest t = extent.createTest(k);
            System.out.println("🧩 Created parent test: " + k);
            return t;
        });

        // Child node (per DataProvider iteration)
        Object[] params = result.getParameters();
        String childName = (params != null && params.length > 0)
                ? Arrays.toString(params)
                : "Default";

        ExtentTest node = parent.createNode("Parameters: " + childName);
        childTest.set(node);
        System.out.println("➡ Created child node for: " + childName);
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        ExtentTest node = childTest.get();
        if (node != null) {
            node.log(Status.PASS, "✅ Test passed successfully");
        }
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        ExtentTest node = childTest.get();
        if (node != null) {
            node.log(Status.FAIL, "❌ Test failed: " + result.getThrowable());
        }
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        ExtentTest node = childTest.get();
        if (node != null) {
            node.log(Status.SKIP, "⚠️ Test skipped: " + result.getThrowable());
        }
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        extent.flush();
        System.out.println("💾 Extent report flushed.");
    }

    // Optional overrides
    @Override public void onStart(ITestContext context) {}
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
}
