package com.reports.accountreports

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.Select

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject



public class TrialBalance {

	/**
	 * Verify Excel File Data
	 *
	 */
	@Keyword
	def verifyTextFileData(String fileName) {
		KeywordUtil.logInfo("Verifying excel file data")
		WebDriver webDriver = DriverFactory.getWebDriver()

		String ProjectDirectory=RunConfiguration.getProjectDir()
		String excelFileLocation=ProjectDirectory+"/Download/"+fileName
		excelFileLocation=excelFileLocation.replace('/', '\\')
		FileInputStream fis=new FileInputStream(new File(excelFileLocation));
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row = sheet.getRow(0);

		KeywordUtil.logInfo("Verifying headers")
		String value=sheet.getRow(0).getCell(4).getStringCellValue().trim()
		if(!value.equals("Trial Balance"))
			KeywordUtil.logInfo("Trial Balance heading is not found. Actual : "+value)

		WebElement element=WebUI.findWebElement(findTestObject('Object Repository/Report/Account Reports/Trial Balance/Select_Type'))
		Select select=new Select(element)
		String expectedValue="Type: "+select.firstSelectedOption.text
		value=sheet.getRow(2).getCell(0).getStringCellValue()
		if(!value.equals(expectedValue))
			KeywordUtil.logInfo(expectedValue+" is not found. Actual : "+value)

		element=WebUI.findWebElement(findTestObject('Object Repository/Report/Account Reports/Trial Balance/Select_Status'))
		select=new Select(element)
		expectedValue="Status : "+select.firstSelectedOption.text
		value=sheet.getRow(3).getCell(0).getStringCellValue()
		if(!value.equals(expectedValue))
			KeywordUtil.logInfo(expectedValue+" is not found. Actual : "+value)

		element=WebUI.findWebElement(findTestObject('Object Repository/Report/Account Reports/Trial Balance/Select_Account'))
		select=new Select(element)
		expectedValue="Accounts :"+select.firstSelectedOption.text
		value=sheet.getRow(2).getCell(2).getStringCellValue()
		if(!value.equals(expectedValue))
			KeywordUtil.logInfo(expectedValue+" is not found. Actual : "+value)

		element=WebUI.findWebElement(findTestObject('Object Repository/Report/Account Reports/Trial Balance/Select_Level'))
		select=new Select(element)
		expectedValue="Level :"+select.firstSelectedOption.text
		value=sheet.getRow(3).getCell(2).getStringCellValue()
		if(!value.equals(expectedValue))
			KeywordUtil.logInfo(expectedValue+" is not found. Actual : "+value)

		element=WebUI.findWebElement(findTestObject('Object Repository/Report/Account Reports/Trial Balance/Input_FromDate'))
		expectedValue="From: "+element.getAttribute("value")
		value=sheet.getRow(2).getCell(1).getStringCellValue()
		if(!value.equals(expectedValue))
			KeywordUtil.logInfo(expectedValue+" is not found. Actual : "+value)

		element=WebUI.findWebElement(findTestObject('Object Repository/Report/Account Reports/Trial Balance/Input_ToDate'))
		expectedValue="To: "+element.getAttribute("value")
		value=sheet.getRow(3).getCell(1).getStringCellValue()
		if(!value.equals(expectedValue))
			KeywordUtil.logInfo(expectedValue+" is not found. Actual : "+value)

		LocalDateTime now = LocalDateTime.now()
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-M-d");
		expectedValue="Exported Date : "+dtf.format(now)
		value=sheet.getRow(1).getCell(4).getStringCellValue().trim()
		if(!value.equals(expectedValue))
			KeywordUtil.logInfo("Exported Date "+expectedValue+" is not found. Actual : "+value)

		Iterator<Row> itr = sheet.iterator();
		int rowCounter=0;
		while (itr.hasNext()) {
			row = itr.next();
			if(rowCounter<4) {
				rowCounter++
				continue;
			}
			int colCounter=0;
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				String expectedtext="";
				String actualText="";
				switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						actualText=cell.getStringCellValue()
						break;
					case Cell.CELL_TYPE_NUMERIC:
						double d=  cell.getNumericCellValue()
						actualText=d<1000?((int)d).toString():d.toString()
						break;
				}
				if(rowCounter==4) {
					if(colCounter<6)
						expectedtext=webDriver.findElement(By.tagName("tr")).findElements(By.tagName("th")).get(colCounter).text.trim();
					else
						expectedtext=webDriver.findElements(By.tagName("tr")).get(1).findElements(By.tagName("th")).get(colCounter-6).text.trim();
				}
				else {
					if(rowCounter==9)
						expectedtext=webDriver.findElement(By.xpath("//tfoot/tr")).findElements(By.tagName("td")).get(colCounter).text.replace("\n","").trim();
					else
						expectedtext=webDriver.findElements(By.xpath("//tbody/tr")).get(rowCounter-5).findElements(By.tagName("td")).get(colCounter).text.replace("\n","").trim();
					if(colCounter>5) {
						expectedtext=expectedtext.replace(",", "")
						actualText=actualText.replace(",", "")
						double d= Double.parseDouble(expectedtext)
						 expectedtext=((int)d).toString()
						 d=Double.parseDouble(actualText)
						 actualText=((int)d).toString()
					}
				}
				if(!expectedtext.replace(" ", "").equals(actualText.replace(" ", "")))
					KeywordUtil.markFailed("Data is not matched in excel sheet. Expected : "+expectedtext+" Actual : "+actualText+" Index : ["+rowCounter+","+colCounter+"]");
				colCounter++;
			}
			KeywordUtil.logInfo("Row # "+(rowCounter+1)+" is verified successfully")
			rowCounter++;
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
			if(i>=6 && i<=8)
				continue
			KeywordUtil.logInfo("Verifying ascending order for column : "+colHeaderWebElements.get(i).text)
			while(!colHeaderWebElements.get(i).getAttribute("aria-sort").equals("ascending")) {
				colHeaderWebElements.get(i).click()
				WebUI.delay(1)
			}
			List<WebElement> colWebElements=null
			if(i>8)
				colWebElements=webDriver.findElements(By.xpath("//td["+(i-2)+"]"))
			else
				colWebElements=webDriver.findElements(By.xpath("//td["+(i+1)+"]"))
			for(int j=0;j<colWebElements.size()-2;j++) {
				boolean isFailed=false;
				String firstRow=colWebElements.get(j).text
				String secondRow=colWebElements.get(j+1).text
				if(i==1||i==2||i==4) {
					continue
				}
				else if(i==5) {
					if(firstRow.toLowerCase().compareTo(secondRow.toLowerCase())>0)
						isFailed=true
				}
				else {
					if(Float.parseFloat(firstRow.replace(",", ""))>Float.parseFloat(secondRow.replace(",", "")))
						isFailed=true
				}
					

				if(isFailed)
					KeywordUtil.markFailed("Ascending Soring is failed for column : "+colHeaderWebElements.get(i).text+"\tCol Index : "+i+" at row # "+(j+1)+"\t"+firstRow+"\t"+secondRow)
			}
		}

		colHeaderWebElements=webDriver.findElements(By.tagName("th"))
		for(int i=0;i<colHeaderWebElements.size();i++) {
			if(i>=6 && i<=8)
				continue
			KeywordUtil.logInfo("Verifying Decending order for column : "+colHeaderWebElements.get(i).text)
			while(!colHeaderWebElements.get(i).getAttribute("aria-sort").equals("descending")) {
				colHeaderWebElements.get(i).click()
				WebUI.delay(1)
			}
			List<WebElement> colWebElements=null
			if(i>8)
				colWebElements=webDriver.findElements(By.xpath("//td["+(i-2)+"]"))
			else
				colWebElements=webDriver.findElements(By.xpath("//td["+(i+1)+"]"))
			for(int j=0;j<colWebElements.size()-2;j++) {
				boolean isFailed=false;
				String firstRow=colWebElements.get(j).text
				String secondRow=colWebElements.get(j+1).text
				if (i==1||i==2||i==4) {
					continue
				}
				else if(i==5) {
					if(firstRow.toLowerCase().compareTo(secondRow.toLowerCase())<0)
						isFailed=true
				}
				else {
					if(Float.parseFloat(firstRow.replace(",", ""))<Float.parseFloat(secondRow.replace(",", "")))
						isFailed=true
				}
				if(isFailed)
					KeywordUtil.markFailed("Decending Soring is failed for column : "+colHeaderWebElements.get(i).text+" at row # "+(j+1))
			}
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
		List<WebElement> elements = webDriver.findElements(By.xpath("//tbody/tr"));
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
	
	/**
	 * Verify visibility of given text in all rows
	 *
	 */
	@Keyword
	def verifyTotal() {
		KeywordUtil.logInfo("Verifying total for all debit and credit")
		WebDriver webDriver = DriverFactory.getWebDriver()
		for(int j=7;j<=12;j++) {
		List<WebElement> elements = webDriver.findElements(By.xpath("//tbody/tr/td["+j+"]"))
		float sum=0;
		for(int i=0;i<elements.size();i++) {
			sum+=Double.parseDouble(elements.get(i).text.replace(",",""))
		}
		String actualTotalStr=webDriver.findElement(By.xpath("//tfoot/tr/td["+j+"]")).text.replace(",","")
		float actualTotal=Double.parseDouble(actualTotalStr)
		if(sum==actualTotal)
			KeywordUtil.markPassed("Total is matched for column "+j)
		else
			KeywordUtil.markFailed("Total is not matched. Actual : "+actualTotal+" Expected : "+sum)
		}
	}
}
