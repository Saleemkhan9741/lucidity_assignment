package com.lucidity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class BaseTest {
    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

    @BeforeSuite(alwaysRun = true)
    public void beforeSuiteSetup() {
        // 1 Clean old reports
        //    → Delete existing Extent Reports, log files, screenshots, etc.

        // 2 Create necessary folders
        //    → e.g., target/reports, target/screenshots, target/logs

        // 3 Setup logging framework
        //    → Initialize Log4j2, set log file path dynamically

        // 4 Start Mock Server or dependent services (if any)
        //    → Optional: automatically spin up containers or mock servers

        // 5 Database connection (if needed)
        //    → Establish DB connection pool or test data setup

        // 6 API authentication setup
        //    → Get and cache access tokens if API requires authentication

        // 7 Print environment summary
        //    → Log or print details like environment, base URL, build ID, etc.
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuiteCleanup() {
        // 1 Flush reports
        //    → Close and save ExtentReport/Allure after test execution

        // 2 Stop Mock Server or containers
        //    → Gracefully shut down mock servers or dockerized dependencies

        // 3 Close DB connections
        //    → Release or close any open DB connections
    }

}