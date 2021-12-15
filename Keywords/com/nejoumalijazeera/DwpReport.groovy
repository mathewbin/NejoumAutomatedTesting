package com.nejoumalijazeera

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.Select

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords






public class DwpReport {

	/**
	 * Verify Show Entries
	 */
	@Keyword
	def verifyShowEntry(TestObject showDropdown, String rowCount) {
		KeywordUtil.logInfo("Verifying row count as per selection")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> elements = webDriver.findElements(By.xpath("//td/parent::tr"))
		WebElement element = WebUiBuiltInKeywords.findWebElement(showDropdown);
		Select selectDropdown=new Select(element);
		if(!selectDropdown.firstSelectedOption.text.equals(rowCount))
			KeywordUtil.markFailed("Expected row count is not matched.")
		else if(elements.size()!=Integer.parseInt(rowCount))
			KeywordUtil.markPassed("Displayed Row count is not matched as per selection")
		else
			KeywordUtil.logInfo("Show Entry Verified for Row Count "+rowCount);
	}
	
	/**
	 * Verify TestData
	 */
	@Keyword
	def verifyRowCount(TestData testdata) {
		KeywordUtil.logInfo("Verifying test data")
		int expectedRowNumbers=testdata.getRowNumbers();
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> elements = webDriver.findElements(By.xpath("//td/parent::tr"))
		int actualRowNumbers=elements.size();
		if(actualRowNumbers!=expectedRowNumbers)
			KeywordUtil.markFailed("Row count is not matched. Actual : "+actualRowNumbers+" Expected : "+expectedRowNumbers);
		else
			KeywordUtil.markPassed("Row count is matched in front end and back end")
	}

	/**
	 * Verify visibility of given text in all rows
	 *
	 */
	@Keyword
	def verifyTextinRows(String text) {
		KeywordUtil.logInfo("Verifying text in all available rows")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> elements = webDriver.findElements(By.xpath("//td/parent::tr"));
		KeywordUtil.logInfo("Total rows"+elements.size())
		boolean isSuccess=true;
		for(int i=0;i<elements.size();i++) {
			if(!elements[i].text.toLowerCase().contains(text.toLowerCase())) {
				isSuccess=false;
				break;
			}
		}
		if(isSuccess)
			KeywordUtil.markPassed("Text match is verified successfully")
		else
			KeywordUtil.markFailed("Text doesn't match")
	}
}
