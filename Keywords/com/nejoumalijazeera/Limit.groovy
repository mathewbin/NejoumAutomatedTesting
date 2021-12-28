package com.nejoumalijazeera

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI





public class Limit {

	/**
	 * Verify TestData
	 */
	@Keyword
	def verifyRowData(TestData testdata) {
		int expectedRowNumbers=testdata.getRowNumbers()
		int expectedColNumbers=testdata.getColumnNumbers()
		WebDriver webDriver = DriverFactory.getWebDriver()
		//Verifying Row Count
		String temp=webDriver.findElement(By.id("myTable_info")).getText().replace("Showing 1 to 10 of ", "").replace(" entries", "");
		int actualRowNumbers=Integer.parseInt(temp);
		if(actualRowNumbers!=expectedRowNumbers)
			KeywordUtil.markFailed("Row count is not matched. Actual : "+actualRowNumbers+" Expected : "+expectedRowNumbers);
		else
			KeywordUtil.markPassed("Row count is matched in front end and back end")
		//Verifying row data
		KeywordUtil.logInfo("Total row # "+expectedRowNumbers)
		int i=1;
		for(int rowCounter=1;rowCounter<=expectedRowNumbers;rowCounter++) {
			KeywordUtil.logInfo("Verifying test data for row # "+rowCounter)
			String actualData=webDriver.findElement(By.xpath("//tr["+i+"]/td[1]")).getText().trim().replace("\n", "")
			int actRowNumber=Integer.parseInt(actualData)
			if(actRowNumber!=rowCounter)
				KeywordUtil.markFailed("Row Number is not matched. Expected : "+rowCounter+" Actual : "+actRowNumber)
			for(int j=1;j<=expectedColNumbers;j++) {
				String expectedData=testdata.getValue(j, i).replaceAll("[^a-zA-Z0-9\\s]", "").trim()
				actualData=webDriver.findElement(By.xpath("//tr["+i+"]/td["+(j+1)+"]")).getText().replaceAll("[^a-zA-Z0-9\\s]", "").trim()
				if(!expectedData.equals(actualData))
					KeywordUtil.markFailed("Data is not matched in excel sheet. Expected : "+expectedData+" Actual : "+actualData+" Index : ["+i+","+j+"]")
			}
			if(i==100) {
				webDriver.findElement(By.linkText("Next")).click()
				i=1;
				WebUI.delay(2)
			}
			i++
		}
	}
}
