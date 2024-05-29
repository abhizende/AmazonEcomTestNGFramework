package pageObjects;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import abstractComponents.AbstractComponent;

public class AmazonProductPage extends AbstractComponent {

	private WebDriver driver;
	private Actions action;

	public AmazonProductPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(linkText = "Visit the Apple Store")
	private WebElement appleStoreLink;

	@FindBy(xpath = "//a/span[text()='Apple Watch']")
	private WebElement appleWatchLink;

	@FindBy(xpath = "//button//span[text()='Quick look']")
	private WebElement quickLook;

	@FindBy(xpath = "//div[@data-testid=\"product-showcase-container\"]//a[contains(@class,'ProductShowcase__title')]")
	private WebElement modalProductTitle;

	@FindBy(xpath = "//div[@data-testid=\"product-showcase-container\"]//a[contains(@class,'ProductShowcase__title')]")
	private WebElement imageProductTitle;

	@FindBy(xpath = "//a[contains(@title,'Apple Watch')]")
	private WebElement productImage;

	By quickLookButton = By.xpath("//button//span[text()='Quick look']");

	public void verifyNewTabOpened(int noOfWindows, int waitDuration) {
		waitUntilNumberOfWindowsToBe(waitDuration, noOfWindows);
	}

	public void switchToNewTab() {
		String currentWindowHandle = driver.getWindowHandle();
		Set<String> allWindowHandles = driver.getWindowHandles();

		for (String handle : allWindowHandles) {
			if (!handle.equals(currentWindowHandle)) {
				driver.switchTo().window(handle);
				break;
			}
		}

	}

	public void clickOnVisitAppleStoreLink() {
		waitUntilElementIsClickable(5000, appleStoreLink);
		appleStoreLink.click();
	}

	public String getimageProductTitle() {
		try {
	        JavascriptExecutor je = (JavascriptExecutor) driver;
	        je.executeScript("arguments[0].scrollIntoView(true);", productImage);
	        waitUntilElementIsVisible(5000, productImage);
	        
	        // JavaScript to hover over the element
	        String mouseOverScript = "var event = new MouseEvent('mouseover', { 'view': window, 'bubbles': true, 'cancelable': true }); arguments[0].dispatchEvent(event);";
	        je.executeScript(mouseOverScript, productImage);
	        
	        return productImage.getAttribute("title");
	    } catch (StaleElementReferenceException e) {
	        System.out.println("Caught StaleElementReferenceException, retrying...");
	        return getimageProductTitle(); // Retry method
	    }
	}

	public void verifyQuickLookDisplayedAndClick() {
		try {
	        JavascriptExecutor je = (JavascriptExecutor) driver;
	        je.executeScript("arguments[0].scrollIntoView(true);", productImage);
	        waitUntilElementIsVisible(5000, quickLookButton);
	        
	        // JavaScript to hover over the element
	        String mouseOverScript = "var event = new MouseEvent('mouseover', { 'view': window, 'bubbles': true, 'cancelable': true }); arguments[0].dispatchEvent(event);";
	        je.executeScript(mouseOverScript, productImage);
	        
	        if (verifyWelementIsDisplayed(quickLook)) {
	            je.executeScript("arguments[0].click();", quickLook);
	        }
	    } catch (StaleElementReferenceException e) {
	        System.out.println("Caught StaleElementReferenceException, retrying...");
	        verifyQuickLookDisplayedAndClick(); // Retry method
	    }
	}

	public boolean verifyModalProuctTitle(String actualText, String expectedText) {
		boolean flag = true;
		String actualTextNoJunkChar = actualText.replaceAll("\\.\\.\\.", "");
		if (expectedText.contains(actualTextNoJunkChar)) {
			flag = true;
		} else {
			flag = false;
		}

		return flag;
	}

	public void selectProductFromDropDownList(String parentProductLink, String childProuctName) {
		WebElement productLink = driver.findElement(By.xpath("//a/span[text()='" + parentProductLink + "']"));
		waitUntilElementIsClickable(5000, productLink);
		action = new Actions(driver);
		action.moveToElement(productLink).click().build().perform();
		List<WebElement> productDropDownList = productLink
				.findElements(By.xpath("following::li[contains(@class,'Navigation__navItem')]"));

		for (WebElement product : productDropDownList) {
			String actualProductName = product.getText();
			if (actualProductName.equalsIgnoreCase(childProuctName)) {
				product.click();
				break;
			}
		}
	}

	public String getModalProductTitle() {
		waitUntilElementIsVisible(5000, modalProductTitle);
		return modalProductTitle.getText();
	}

}
