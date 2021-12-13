package com.nejoumalijazeera

import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory


class StatementOfCarsArriving {
	/**
	 * Verify amount 0.00 for Remaining column for Paid status 
	 */
	@Keyword
	def verifyRemainingColumnsForPaidStatus() {
		KeywordUtil.logInfo("Verifying rows for Paid status")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> elements = webDriver.findElements(By.xpath("//td[23]"))
		KeywordUtil.logInfo("Total rows"+elements.size())
		boolean isSuccess=true;
		for(int i=0;i<elements.size();i++) {
			if(!elements[i].text.equals("0.00")) {
				isSuccess=false;
				break;
			}
		}
		if(isSuccess)
			KeywordUtil.markPassed("Remaining column value is verified successfully")
		else
			KeywordUtil.markFailed("Remaining column is non-zero")
	}

	/**
	 * Verify amount 0.00 for Remaining column for Paid status
	 */
	@Keyword
	def verifyRemainingColumnsForUnPaidStatus() {
		KeywordUtil.logInfo("Verifying rows for UnPaid status")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> elements = webDriver.findElements(By.xpath("//td[23]"))
		KeywordUtil.logInfo("Total rows"+elements.size())
		boolean isSuccess=true;
		for(int i=0;i<elements.size();i++) {
			if(elements[i].text.equals("0.00")) {
				isSuccess=false;
				break;
			}
		}
		if(isSuccess)
			KeywordUtil.markPassed("Remaining column value is verified successfully")
		else
			KeywordUtil.markFailed("Remaining column is zero")
	}

	/**
	 * Dismiss unnecessary notication  
	 */
	@Keyword
	def DismissUnnecessaryNotification() {
		KeywordUtil.logInfo("Dismissing unnecessary notification")
		RunConfiguration.setWebDriverPreferencesProperty("download.default_directory", "C:\\Users\\somkant.shrivastava\\git\\NejoumAutomatedTesting\\Download");
		WebDriver webDriver = DriverFactory.getWebDriver()

		webDriver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE)
		while(webDriver.findElements(By.xpath("//button[text()='OK']")).size()>0) {
			webDriver.findElement(By.xpath("//button[text()='OK']")).click()
		}
	}

	/**
	 * Verify visibility of given text in all rows
	 * 
	 */
	@Keyword
	def verifyTextinRows(String text) {
		KeywordUtil.logInfo("Verifying text in all available rows")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> elements = webDriver.findElements(By.xpath("//tr[not(position() > last() -1)]"));
		KeywordUtil.logInfo("Total rows"+elements.size())
		boolean isSuccess=true;
		for(int i=0;i<elements.size();i++) {
			if(!elements[i].text.toLowerCase().contains(text.toLowerCase())) {
				isSuccess=false;
				break;
			}
		}
		if(isSuccess)
			KeywordUtil.markPassed("Remaining column value is verified successfully")
		else
			KeywordUtil.markFailed("Remaining column is non-zero")
	}
}