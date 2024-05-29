package pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import abstractComponents.AbstractComponent;

public class AmazonHomePage extends AbstractComponent {

	private WebDriver driver;
	
	public AmazonHomePage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(id = "searchDropdownBox")
	private WebElement productSearchDropdownBox;

	@FindBy(id = "twotabsearchtextbox")
	private WebElement searchBoxText;

	@FindBy(xpath = "//div[@class='autocomplete-results-container']//div[@role='button']")
	private List<WebElement> searchResultList;

	@FindBy(xpath = "//h2/a/span")
	private List<WebElement> productResultList;

	private By resultsList = By.xpath("//h2/a/span");

	private By searchList = By.xpath("//div[@class='autocomplete-results-container']//div[@role='button']");

	public void selectProductFromDropdownBox(String categoryName) throws InterruptedException {
		Select select = new Select(productSearchDropdownBox);
		select.selectByVisibleText(categoryName);
	}

	public void enterProductSearchText(String productName) {
		waitUntilElementIsClickable(5000, searchBoxText);
		searchBoxText.click();
		searchBoxText.clear();
		searchBoxText.sendKeys(productName);
	}

	public List<WebElement> getSearchResultList() {
		waitUntilElementIsVisible(10000, searchList);
		return searchResultList;
	}

	public List<WebElement> getProductResultsList() {
		waitUntilElementIsVisible(5000, resultsList);
		return productResultList;
	}

	public boolean verifyProductPresentInSearchResultList(String productName) {
		boolean flag = false;

		for (WebElement product : getSearchResultList()) {
			String actualProductName = product.getText().toLowerCase();
			if (actualProductName.contains(productName.toLowerCase())) {
				flag = true;
			} else {
				flag = false;
			}
		}
		return flag;
	}

	public void selectProductFromSearchList(String productName) throws InterruptedException {
		Actions action = new Actions(driver);

		for (WebElement product : getSearchResultList()) {
			// Retry mechanism to handle stale element reference
			for (int retry = 0; retry < 2; retry++) { // Retry up to 2 times
				try {
					enterProductSearchText(productName);
					// Re-locate the search result list to avoid stale element
					List<WebElement> searchResults = getSearchResultList();
					waitUntilllAllElementsAreVisible(5000, searchResults);

					for (WebElement updatedProduct : searchResults) {
						waitUntilElementIsVisible(5000, updatedProduct); // Wait for updated search results
						String actualProductName = updatedProduct.getText();
						if (actualProductName.equalsIgnoreCase(productName)) {
							// Scroll element into view if necessary
							action.moveToElement(updatedProduct).perform();
							waitUntilElementIsClickable(5000, updatedProduct);
							updatedProduct.click();
							return; // Exit method after clicking the product
						}
					}
					break; // Break out of retry loop if no exception occurs
				} catch (StaleElementReferenceException e) {
					// Log the exception and retry
					System.out.println("Caught StaleElementReferenceException, retrying...");
					Thread.sleep(500);
				}
			}
		}
	}

	public AmazonProductPage clickOnSearchedProduct(String productName) {
		for (WebElement product : getProductResultsList()) {
			String actualProductName = product.getText().toLowerCase();
			if (actualProductName.contains(productName.toLowerCase())) {
				waitUntilElementIsClickable(5000, product);
				product.click();
				break;
			}
		}
		return new AmazonProductPage(driver);
	}

	public void navitageToLandingPage(String url) {
		driver.get(url);
	}

}
