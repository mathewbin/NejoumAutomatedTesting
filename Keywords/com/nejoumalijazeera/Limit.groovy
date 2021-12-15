package com.nejoumalijazeera

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory







public class Limit {
	
	/**
	 * Verify TestData
	 */
	@Keyword
	def verifyRowCount(TestData testdata) {
		KeywordUtil.logInfo("Verifying test data")
		int expectedRowNumbers=testdata.getRowNumbers();
		WebDriver webDriver = DriverFactory.getWebDriver()
		String temp=webDriver.findElement(By.id("myTable_info")).getText().replace("Showing 1 to 10 of ", "").replace(" entries", "");		
		int actualRowNumbers=Integer.parseInt(temp);
		if(actualRowNumbers!=expectedRowNumbers)
			KeywordUtil.markFailed("Row count is not matched. Actual : "+actualRowNumbers+" Expected : "+expectedRowNumbers);
		else
			KeywordUtil.markPassed("Row count is matched in front end and back end")
	}
}
