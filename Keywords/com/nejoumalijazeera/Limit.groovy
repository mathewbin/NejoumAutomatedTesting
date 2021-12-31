package com.nejoumalijazeera

import java.text.SimpleDateFormat

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

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

	/**
	 * Verifying order
	 */
	@Keyword
	def VerifyingOrder() {
		WebDriver webDriver = DriverFactory.getWebDriver()
		// Verify No column
		List<WebElement> colHeaderWebElements=webDriver.findElements(By.tagName("th"))
		for(int i=0;i<colHeaderWebElements.size();i++) {
			KeywordUtil.logInfo("Verifying ascending order for column : "+colHeaderWebElements.get(i).text)
			while(!colHeaderWebElements.get(i).getAttribute("aria-sort").equals("ascending")) {
				colHeaderWebElements.get(i).click()
				WebUI.delay(1)
			}
			List<WebElement> colWebElements=webDriver.findElements(By.xpath("//td["+(i+1)+"]"))
			for(int j=0;j<colWebElements.size()-1;j++) {
				boolean isFailed=false;
				String firstRow=colWebElements.get(j).text.replace(",","")
				String secondRow=colWebElements.get(j+1).text.replace(",","")
				if(i==2||i==3||i==4) {
					firstRow=firstRow.replaceAll("[^a-zA-Z0-9\\s]", "").trim().toLowerCase()
					secondRow=secondRow.replaceAll("[^a-zA-Z0-9\\s]", "").trim().toLowerCase()
					if(firstRow.compareTo(secondRow)>0)
						isFailed=true
				}
				else {
					float f1=Float.parseFloat(firstRow)
					float f2=Float.parseFloat(secondRow)
					if(f1>f2)
						isFailed=true
				}
				if(isFailed)
					KeywordUtil.markFailedAndStop("Ascending Sorting is failed for column : "+colHeaderWebElements.get(i).text+" at row # "+(j+1)+"\t"+firstRow+"\t"+secondRow)
			}
		}

		colHeaderWebElements=webDriver.findElements(By.tagName("th"))
		for(int i=0;i<colHeaderWebElements.size()-1;i++) {
			KeywordUtil.logInfo("Verifying Decending order for column : "+colHeaderWebElements.get(i).text)
			while(!colHeaderWebElements.get(i).getAttribute("aria-sort").equals("descending")) {
				colHeaderWebElements.get(i).click()
				WebUI.delay(1)
			}
			List<WebElement> colWebElements=webDriver.findElements(By.xpath("//td["+(i+1)+"]"))
			for(int j=0;j<colWebElements.size()-1;j++) {
				boolean isFailed=false;
				String firstRow=colWebElements.get(j).text.replace(",","")
				String secondRow=colWebElements.get(j+1).text.replace(",","")
				if(i==2||i==3||i==4) {
					firstRow=firstRow.replaceAll("[^a-zA-Z0-9\\s]", "").trim().toLowerCase()
					secondRow=secondRow.replaceAll("[^a-zA-Z0-9\\s]", "").trim().toLowerCase()
					if(firstRow.compareTo(secondRow)<0)
						isFailed=true
				}
				else{
					float f1=Float.parseFloat(firstRow)
					float f2=Float.parseFloat(secondRow)
					if(f1<f2)
						isFailed=true
				}
				if(isFailed)
					KeywordUtil.markFailedAndStop("Decending Sorting is failed for column : "+colHeaderWebElements.get(i).text+" at row # "+(j+1)+firstRow+"\t"+secondRow)
			}
		}
	}
}
