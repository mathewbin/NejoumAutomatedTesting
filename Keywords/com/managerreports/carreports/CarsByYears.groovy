package com.managerreports.carreports

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.Select

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI




public class CarsByYears {
	/**
	 * Verify visibility of given text in all rows
	 *
	 */
	@Keyword
	def verifyTextinRows() {
		KeywordUtil.logInfo("Verifying text in all available rows")
		WebDriver webDriver = DriverFactory.getWebDriver()
		int totalRows=Integer.parseInt(webDriver.findElement(By.id("tableLengthAndBTN_info")).text.split(" ")[5])
		if(totalRows==0)
			return
		List<WebElement> elements = webDriver.findElements(By.xpath("//td[3]"));
		Random random=new Random()
		int randomNumber=random.nextInt(elements.size())
		String text=elements.get(randomNumber).text.split(" ")[0]
		KeywordUtil.logInfo("Total rows"+elements.size())
		WebUI.sendKeys(findTestObject("Object Repository/Manager Reports/Car Reports/CarInsertedToday/SearchInputTxt"), text)
		WebUI.delay(2)
		elements = webDriver.findElements(By.xpath("//td[3]"));
		boolean isSuccess=true;
		for(int i=0;i<elements.size();i++) {
			if(!elements[i].text.toLowerCase().contains(text.toLowerCase())) {
				isSuccess=false;
				break;
			}
		}
		if(isSuccess)
			KeywordUtil.markPassed("Text verified successfully")
		else
			KeywordUtil.markFailed("Text verification failed")
	}
	
	/**
	 * Verify Show Entries
	 */
	@Keyword
	def verifyShowEntry(int rowCount) {
		KeywordUtil.logInfo("Verifying row count as per selection")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> elements = webDriver.findElements(By.xpath("//td/parent::tr"))
		WebElement element = WebUI.findWebElement(findTestObject("Object Repository/Manager Reports/Car Reports/CarInsertedToday/ShowDropdown"));
		Select selectDropdown=new Select(element);
		int totalRows=Integer.parseInt(webDriver.findElement(By.id("tableLengthAndBTN_info")).text.split(" ")[5])
		if(totalRows<rowCount)
			return
		if(!selectDropdown.firstSelectedOption.text.equals(rowCount.toString()))
			KeywordUtil.markFailed("Expected row count is not matched."+selectDropdown.firstSelectedOption.text)
		else if(elements.size()!=rowCount)
			KeywordUtil.markPassed("Displayed Row count is not matched as per selection")
		else
			KeywordUtil.logInfo("Show Entry Verified for Row Count "+rowCount);
	}

	
}
