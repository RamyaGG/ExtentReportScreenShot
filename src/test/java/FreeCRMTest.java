import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/*
 * How to add Screenshot in Extent Report for Failed Test Cases in Selenium:

Learn:
+ How to generate Extent Report
+How to add screenshot for failure test cases
+Capture Screenshot in Extent Reports – Selenium Webdriver 
+How to add Error/Exceptions logs for failure test cases
+ Generate Test Result DashBoard
+ how to generate extent report in selenium webdriver
+ Selenium WebDriver || Test Suite Execution and Reports Generation
 * */

public class FreeCRMTest {
	public WebDriver driver;
	public ExtentReports extent;
	public ExtentTest extentTest;

	@BeforeTest
	public void setExtent() {

		extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/ExtentReport.html", true); // true is for overriding the existing one
		extent.addSystemInfo("Host Name", "Ramya GG");
		extent.addSystemInfo("Environment", "QA");

	}
	
	@AfterTest
	public void endReport() {
		extent.flush();
		extent.close();
	}


	public static String getScreenShot(WebDriver driver, String screenShotName) throws IOException {
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String destination = System.getProperty("user.dir")+ "\\FailedTestScreenshots\\" + screenShotName + dateName + ".png";
		File finalDestination = new File(destination);
		
		FileUtils.copyFile(scrFile, finalDestination);
		return destination;
		
		
	}
	
	@BeforeMethod
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "H:\\Edureka\\Selenium\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get("https://www.freecrm.com");

	}

	@Test
	public void freeCRMTitleTest() {
		extentTest = extent.startTest("freeCRMTitleTest");
		String title = driver.getTitle();
		Assert.assertEquals(title, "123Free CRM in the cloud software boosts sales");
	}
	
	@Test
	public void freeCRMLogoTest() {
		extentTest = extent.startTest("freeCRMLogoTest");
		boolean a = driver.findElement(By.xpath("//img[contains(@class,'12img-responsive')]")).isDisplayed();
		Assert.assertTrue(a);
	}
	
	

	@AfterMethod
	public void tearDown(ITestResult result) throws IOException {
		if(result.getStatus()==ITestResult.FAILURE) {// failure test cases in testng are defined by 2
		extentTest.log(LogStatus.FAIL,"TEST CASE FAILED IS "+result.getName()); // add name in extent report
		extentTest.log(LogStatus.FAIL,"TEST CASE FAILED IS "+result.getThrowable()); // add error/exception in extent report
		
		String screenShotPath = FreeCRMTest.getScreenShot(driver, result.getName());
		extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(screenShotPath)); // add screen shot in extent report . addScreenCapture is for adding videos in extent report
		
		}else if(result.getStatus()==ITestResult.SKIP) { // for skip no is 3. for pass no is 1
		extentTest.log(LogStatus.SKIP, "TEST CASE SKIPPED IS"+result.getName());
		
		}else if(result.getStatus()==ITestResult.SUCCESS) {
			extentTest.log(LogStatus.PASS, "TEST CASE Passed IS"+result.getName());
		}
		
		extent.endTest(extentTest); // ending test and ends the current test  and prepare to create html report
		driver.quit();
	}

}
