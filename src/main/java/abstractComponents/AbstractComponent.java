package abstractComponents;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AbstractComponent {

	WebDriver driver;

	public AbstractComponent(WebDriver driver) {
		// TODO Auto-generated constructor stub
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//button[@routerlink='/dashboard/cart']")
	WebElement cartIconElement;

	@FindBy(xpath = "//button[@routerlink='/dashboard/myorders']")
	WebElement ordersIconElement;

	public void waitUntilNumberOfWindowsToBe(int waitDuration, int expectedNoOfWindows) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration));
		wait.until(ExpectedConditions.numberOfWindowsToBe(expectedNoOfWindows));
	}

	public void waitUntilElementIsVisible(int waitDuration, By locatedBy) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration));
		wait.until(ExpectedConditions.visibilityOfElementLocated(locatedBy));
		wait.until(ExpectedConditions.presenceOfElementLocated(locatedBy));
	}

	public void waitUntilllAllElementsAreVisible(int waitDuration, List<WebElement> elements) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration));
		wait.until(ExpectedConditions.visibilityOfAllElements(elements));
	}

	public void waitUntilElementIsVisible(int waitDuration, WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration));
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public void waitUntilElementIsNotVisible(int waitDuration, WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration));
		wait.until(ExpectedConditions.invisibilityOf(element));
	}

	public void waitUntilElementIsClickable(int waitDuration, WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration));
		wait.until(ExpectedConditions.elementToBeClickable(element));

	}

	public Boolean verifyWelementIsDisplayed(WebElement element) {
		return element.isDisplayed();
	}
}
