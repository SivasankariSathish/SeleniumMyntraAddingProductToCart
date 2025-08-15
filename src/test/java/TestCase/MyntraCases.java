package TestCase;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import PageObject.HomePageFactory;
import Utilities.DataProviders;
import Utilities.ExtentReportManager;

public class MyntraCases{
	
	public WebDriver driver;
	 private final Logger logger = LogManager.getLogger(getClass());
	 public Properties prop;
	 
	@BeforeClass
	@Parameters({"browser"})
	public void setup(String br) throws InterruptedException, IOException
	{
		
		//optionss.addArguments("disable-notifications");
		switch(br)
		{
		case "chrome" :
			ChromeOptions cp= new ChromeOptions();

			HashMap<String,Object> prefs= new HashMap<String,Object>();
			
			prefs.put("profile.default_content_setting_values.notifications", 2);
			cp.setExperimentalOption("prefs", prefs);
			driver= new ChromeDriver(cp);
			break;
		case "microsoftedge" : 
			EdgeOptions ep = new EdgeOptions();
		    ep.addArguments("start-maximized", "inprivate");
		    ep.setAcceptInsecureCerts(true);
		    driver = new EdgeDriver(ep);
			//driver=new EdgeDriver();
			break;
			
		case "firefox":
			FirefoxOptions fp = new FirefoxOptions();
			fp.addPreference("dom.webnotifications.enabled", false);
			//fp.addArguments("disable-notifications");
			driver=new FirefoxDriver(fp);
		default: throw new IllegalArgumentException("Browser not supported: " + br);
			     
		}
		
		
		driver.manage().window().maximize();
		ExtentReportManager.setDriver(driver);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
		FileReader file=new FileReader("./src//main//resources//config.properties");
		
		//logger=LogManager.getLogger(this.getClass());
		prop=new Properties();
		prop.load(file);
		System.out.println(prop.getProperty("baseURL"));
		driver.get(prop.getProperty("baseURL"));
		Thread.sleep(5000);
		logger.info("*************************setup is done****************************");
	}
	
	@Test(priority=1,dataProvider="productList",dataProviderClass=DataProviders.class)
	public void addingProductIncart(String productForSelection) throws InterruptedException  
	{
		
	
		logger.info("****************Adding Product in Cart********************");
		HomePageFactory hp= new HomePageFactory(driver);
		String randomString= RandomStringUtils.randomAlphabetic(6);
		hp.catogorySelection(randomString);
		logger.info("Category Selection");
		hp.productSelection(productForSelection);
		logger.info("Product Selection");
		hp.itemSelection();
		logger.info("Item Selection");
		hp.addingInCart();
		logger.info("adding into cart");
		hp.clearAllProduct();
		;
		}
		
	
	@Test(priority=2,dependsOnMethods="addingProductIncart")
	public void checkingPriceValue() throws InterruptedException
	{
		HomePageFactory hp= new HomePageFactory(driver);
		String totalAmount=hp.checkingCartBag();
		logger.info("***************Total Amount in Cart********************");
		System.out.println("Total Amount To Pay:"+totalAmount);
		//Assert.fail("myElement not found");
	}
	
	@AfterClass
	public void tearDown() throws InterruptedException
	{
		
		driver.quit();
		logger.info("*************************************Done with Execution****************************");
	}


 
    
	
}
