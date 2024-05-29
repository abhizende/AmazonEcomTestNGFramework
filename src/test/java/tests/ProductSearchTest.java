package tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.AmazonHomePage;
import pageObjects.AmazonProductPage;
import testComponents.BaseTest;

public class ProductSearchTest extends BaseTest {

    @Test(dataProvider = "getData")
    public void validateProductSearch(HashMap<String, String> testData) throws IOException, InterruptedException {

        AmazonHomePage amazonHomePage = launchApplication();

        amazonHomePage.selectProductFromDropdownBox(testData.get("category"));
        amazonHomePage.enterProductSearchText(testData.get("productName1"));

        boolean verifyProductInSearchResults = amazonHomePage.verifyProductPresentInSearchResultList(testData.get("productName1"));
        Assert.assertTrue(verifyProductInSearchResults);

        amazonHomePage.selectProductFromSearchList(testData.get("productName2"));

        AmazonProductPage productPage = amazonHomePage.clickOnSearchedProduct(testData.get("productName1"));
        productPage.verifyNewTabOpened(2, 5000);
        productPage.switchToNewTab();
        productPage.clickOnVisitAppleStoreLink();
        productPage.selectProductFromDropDownList(testData.get("parentProduct"), testData.get("subProductName"));

        String expectedProductTitle = productPage.getimageProductTitle();

        productPage.verifyQuickLookDisplayedAndClick();

        String modalProductTitle = productPage.getModalProductTitle();

        Boolean productTitle = productPage.verifyModalProuctTitle(modalProductTitle, expectedProductTitle);

        Assert.assertTrue(productTitle);
    }
    
    @DataProvider
	public Object[][] getData() throws IOException {

		String filePath = System.getProperty("user.dir") + "/src/test/java/testData/AmazonProductSearch.json";
		List<HashMap<String, String>> testData = getJsonDataToMap(filePath);

		return new Object[][] { { testData.get(0) } };
	}
}