package Utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager implements ITestListener, ISuiteListener {

	 private static WebDriver driver;

	    public static void setDriver(WebDriver driverRef) {
	        driver = driverRef;
	    }
    private static ExtentReports extent;
    private static Map<Long, ExtentTest> testMap = new HashMap<>();

    /**
     * Runs before the suite starts - create Extent report
     */
    @Override
    public void onStart(ISuite suite) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String reportPath = System.getProperty("user.dir") + "/reports/ExtentReport_" + timestamp + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setDocumentTitle("Automation Test Report");
        spark.config().setReportName("Selenium TestNG Results");
        spark.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(spark);

        // Add system info
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Tester", "Automation QA");
    }

    /**
     * Runs after the suite ends - flush report
     */
    @Override
    public void onFinish(ISuite suite) {
        if (extent != null) {
            extent.flush();
        }
    }

    /**
     * When each test starts
     */
    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName(),
                result.getMethod().getDescription());
        testMap.put(Thread.currentThread().getId(), test);
    }

    /**
     * On test pass
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        getTest().pass("Test Passed");
    }

    /**
     * On test failure
     */
    @Override
    public void onTestFailure(ITestResult result) {
       // getTest().fail("Test Failed: " + result.getThrowable());
        // Screenshot can be attached here if needed
        takeScreenshot(result.getName());
    }
    private void takeScreenshot(String testName) {
        if (driver != null) {
            try {
                File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

                // Create /screenshots folder if missing
                String folderPath = System.getProperty("user.dir") + "/screenshots";
                File folder = new File(folderPath);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                // Timestamp for unique filename
                String timestamp = new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss").format(new Date());
                String filePath = folderPath + "/" + testName + "_" + timestamp + ".png";

                FileUtils.copyFile(srcFile, new File(filePath));
                System.out.println("✅ Screenshot saved at: " + filePath);
            } catch (IOException e) {
                System.out.println("❌ Failed to save screenshot: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ WebDriver is null in Listener — screenshot not taken!");
        }
    }

    /**
     * On test skipped
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        getTest().skip("Test Skipped");
    }

    /**
     * Utility: get current test instance for thread
     */
    private static ExtentTest getTest() {
        return testMap.get(Thread.currentThread().getId());
    }
}
