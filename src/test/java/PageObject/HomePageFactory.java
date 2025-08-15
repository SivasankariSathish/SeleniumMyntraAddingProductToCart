package PageObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePageFactory{
	WebDriver driver;
	
	public HomePageFactory(WebDriver driver)
	{
		this.driver=driver;
		PageFactory.initElements(driver,this);
	}

	@FindBy(xpath="//input[@placeholder='Search for products, brands and more']")
	WebElement send;
	@FindBy(xpath="//a[text()='Beauty']")
	WebElement category;
	@FindBy(xpath="//div[@class='categories-more']")
	WebElement categoryMore;
	@FindBy(xpath="//input[@placeholder='Search categories']")
	WebElement product;
	@FindBy(xpath="//ul[@class='FilterDirectory-list']/li")
	List<WebElement> selectionList;
	@FindBy(xpath="//ul[@class='results-base']/li")
	List<WebElement> itemList;
	@FindBy(xpath="//div[@class='pdp-details common-clearfix']/div[2]/div[2]//div[text()='ADD TO BAG']")
	WebElement cart;
	@FindBy(xpath="//div[@class='vertical-filters-filters header-container']//span[@class='header-clearAllBtn']")
	WebElement clearProduct;
	@FindBy(xpath="//span[text()='Bag']")
	WebElement cartBag;
	@FindBy(xpath="//div[@class='priceBreakUp-base-orderSummary']//div[5]//span[2]")
	WebElement totalAmmountToPay;
	
	
	public void catogorySelection(String randomString)
	{
		send.sendKeys(randomString);
		category.click();
		categoryMore.click();
	}
	
	public void productSelection(String productForSelection) throws InterruptedException
	{
		product.sendKeys(productForSelection);
		Thread.sleep(3000);
		for(WebElement selectioneach:selectionList)
		{
			selectioneach.click();
			
		}
		driver.findElement(By.xpath("//span[@class='myntraweb-sprite FilterDirectory-close sprites-remove']")).click();
		Thread.sleep(2000);
	}
	
	public void itemSelection()
	{
		itemList.get(0).click();
		Set<String> windows=driver.getWindowHandles();
		List<String> windowsarr= new ArrayList<String>(windows);
		String redirect= windowsarr.get(1);
		driver.switchTo().window(redirect);
	}
	
	public void addingInCart() throws InterruptedException
	{
		cart.click();
		Thread.sleep(3000);
		Set<String> windows=driver.getWindowHandles();
		List<String> windowsarr= new ArrayList<String>(windows);
		String parent= windowsarr.get(0);
		driver.close();
		driver.switchTo().window(parent);
	}
	
	public void clearAllProduct()
	{
		clearProduct.click();
	}
	
   public String checkingCartBag() throws InterruptedException
   {
	   cartBag.click();
	   Thread.sleep(1000);
	   String totalAmount=totalAmmountToPay.getText();
	   return totalAmount;
	   
	   
   }
  
	
}
