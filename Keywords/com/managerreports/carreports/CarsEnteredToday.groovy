package com.managerreports.carreports

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.Select

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI



public class CarsEnteredToday {

	/**
	 * Verify Copy Button
	 */
	@Keyword
	def VerifyCopyButton() {
		KeywordUtil.logInfo("Verifying Copy Button")
		StringSelection stringSelection = new StringSelection("")
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
				stringSelection, null)
		Toolkit toolkit = Toolkit.getDefaultToolkit()
		Clipboard clipboard = toolkit.getSystemClipboard()
		String resultBeforeClick = (String) clipboard.getData(DataFlavor.stringFlavor)
		if(!resultBeforeClick.isEmpty())
			KeywordUtil.markFailed("Clipboard text is not yet cleaned : "+resultBeforeClick)
		WebUI.click(findTestObject('Object Repository/Manager Reports/Car Reports/CarInsertedToday/CopyButton'))
		WebUI.delay(1)
		String resultAfterClick = (String) clipboard.getData(DataFlavor.stringFlavor)
		if(resultBeforeClick.equals(resultAfterClick))
			KeywordUtil.markFailed("Unexpected clipboard text is found"+resultBeforeClick)
	}

	/**
	 * Verifying order
	 */
	@Keyword
	def VerifyingOrder() {
		WebDriver webDriver = DriverFactory.getWebDriver()
		// Verify No column
		List<WebElement> colHeaderWebElements=webDriver.findElements(By.tagName("th"))
		for(int i=0;i<colHeaderWebElements.size();i++) {
			if(i==1)
				continue
			KeywordUtil.logInfo("Verifying ascending order for column : "+colHeaderWebElements.get(i).text)
			while(!colHeaderWebElements.get(i).getAttribute("aria-sort").equals("ascending")) {
				colHeaderWebElements.get(i).click()
				WebUI.delay(5)
			}
			List<WebElement> colWebElements=webDriver.findElements(By.xpath("//td["+(i+1)+"]"))
			for(int j=0;j<colWebElements.size()-1;j++) {
				boolean isFailed=false;
				String firstRow=colWebElements.get(j).text.replace(",","")
				String secondRow=colWebElements.get(j+1).text.replace(",","")
				float f1=Float.parseFloat(firstRow)
				float f2=Float.parseFloat(secondRow)
				if(f1>f2)
					isFailed=true
				if(isFailed)
					KeywordUtil.markFailedAndStop("Ascending Sorting is failed for column : "+colHeaderWebElements.get(i).text+" at row # "+(j+1)+"\t"+firstRow+"\t"+secondRow)
			}
		}

		colHeaderWebElements=webDriver.findElements(By.tagName("th"))
		for(int i=0;i<colHeaderWebElements.size()-1;i++) {
			if(i==1)
				continue;
			KeywordUtil.logInfo("Verifying Decending order for column : "+colHeaderWebElements.get(i).text)
			while(!colHeaderWebElements.get(i).getAttribute("aria-sort").equals("descending")) {
				colHeaderWebElements.get(i).click()
				WebUI.delay(5)
			}
			List<WebElement> colWebElements=webDriver.findElements(By.xpath("//td["+(i+1)+"]"))
			for(int j=0;j<colWebElements.size()-1;j++) {
				boolean isFailed=false;
				String firstRow=colWebElements.get(j).text.replace(",","")
				String secondRow=colWebElements.get(j+1).text.replace(",","")
				float f1=Float.parseFloat(firstRow)
				float f2=Float.parseFloat(secondRow)
				if(f1<f2)
					isFailed=true
				if(isFailed)
					KeywordUtil.markFailedAndStop("Decending Sorting is failed for column : "+colHeaderWebElements.get(i).text+" at row # "+(j+1)+firstRow+"\t"+secondRow)
			}
		}
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
		if(!selectDropdown.firstSelectedOption.text.equals(rowCount))
			KeywordUtil.markFailed("Expected row count is not matched.")
		else if(elements.size()!=Integer.parseInt(rowCount))
			KeywordUtil.markPassed("Displayed Row count is not matched as per selection")
		else
			KeywordUtil.logInfo("Show Entry Verified for Row Count "+rowCount);
	}

	/**
	 * Verify visibility of given text in all rows
	 *
	 */
	@Keyword
	def verifyTextinRows() {
		KeywordUtil.logInfo("Verifying text in all available rows")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> elements = webDriver.findElements(By.xpath("//td[2]"));
		Random random=new Random()
		int randomNumber=random.nextInt(elements.size())
		String text=elements.get(randomNumber).text.split(" ")[0]
		KeywordUtil.logInfo("Total rows"+elements.size())
		WebUI.sendKeys(findTestObject("Object Repository/Manager Reports/Car Reports/CarInsertedToday/SearchInputTxt"), text)
		WebUI.delay(2)
		elements = webDriver.findElements(By.xpath("//td[2]"));
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
	 * Verify pagination
	 *
	 */
	@Keyword
	def verifyPagination() {
		KeywordUtil.logInfo("Verifying Pagination")
		WebDriver webDriver = DriverFactory.getWebDriver()
		WebElement selectedDropdownValue = WebUI.findWebElement(findTestObject("Object Repository/Manager Reports/Car Reports/CarInsertedToday/ShowDropdown"));
		Select selectDropdown=new Select(selectedDropdownValue);
		int perPageEntry=Integer.parseInt(selectDropdown.firstSelectedOption.text)
		int totalRows=Integer.parseInt(webDriver.findElement(By.id("tableLengthAndBTN_info")).text.split(" ")[5])
		WebElement previousButton=WebUI.findWebElement(findTestObject("Object Repository/Manager Reports/Car Reports/CarInsertedToday/PreviousButton"));
		WebElement nextButton=WebUI.findWebElement(findTestObject("Object Repository/Manager Reports/Car Reports/CarInsertedToday/NextButton"));
		if(!previousButton.getAttribute("class").contains("disabled"))
			KeywordUtil.markFailed("Previous button is enable")
		int counter=perPageEntry>totalRows?totalRows:perPageEntry
		while(counter<totalRows) {
			nextButton.click()
			nextButton=WebUI.findWebElement(findTestObject("Object Repository/Manager Reports/Car Reports/CarInsertedToday/NextButton"));
			WebUI.delay(1)
			counter+=perPageEntry
		}
		if(!nextButton.getAttribute("class").contains("disabled"))
			KeywordUtil.markFailed("Next button is enable")
		previousButton=WebUI.findWebElement(findTestObject("Object Repository/Manager Reports/Car Reports/CarInsertedToday/PreviousButton"));
		while(!previousButton.getAttribute("class").contains("disabled")) {
			previousButton.click()
			WebUI.delay(1)
			previousButton=WebUI.findWebElement(findTestObject("Object Repository/Manager Reports/Car Reports/CarInsertedToday/PreviousButton"));
		}
	}
}
