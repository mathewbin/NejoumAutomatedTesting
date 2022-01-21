package com.managerreports.autoreports

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

import com.kms.katalon.core.annotation.Keyword
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI





public class CarNotNormal {

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
		String value=sheet.getRow(1).getCell(1).getStringCellValue().trim()
		if(!value.equals("From :"))
			KeywordUtil.logInfo("From : is not found. Actual : "+value)
		value=sheet.getRow(2).getCell(1).getStringCellValue().trim()
		if(!value.equals("To :"))
			KeywordUtil.logInfo("To : is not found. Actual : "+value)
		LocalDateTime now = LocalDateTime.now()
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-M-d");
		String expectedValue="Exported Date : "+dtf.format(now)
		value=sheet.getRow(1).getCell(2).getStringCellValue().trim()
		if(!value.equals(expectedValue))
			KeywordUtil.logInfo("Exported Date "+expectedValue+" is not found. Actual : "+value)
		expectedValue="search by lotnumber ,vin , customer , container:"
		value=sheet.getRow(2).getCell(2).getStringCellValue().trim()
		if(!value.equals(expectedValue))
			KeywordUtil.logInfo(expectedValue+" is not found. Actual : "+value)

		Iterator<Row> itr = sheet.iterator();
		int rowCounter=0;
		while (itr.hasNext()) {
			row = itr.next();
			if(rowCounter<3) {
				rowCounter++
				continue;
			}
			int colCounter=0;
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				if((rowCounter==3 && colCounter==8)||(rowCounter>3 && (colCounter==1||colCounter>6))) {
					colCounter++
					continue
				}
				String expectedtext="";
				String actualText="";
				switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						actualText=cell.getStringCellValue()
						break;
					case Cell.CELL_TYPE_NUMERIC:
						double d=  cell.getNumericCellValue()
						actualText=((int)d).toString()
						break;
				}
				if(rowCounter==3) {
					expectedtext=webDriver.findElement(By.tagName("tr")).findElements(By.tagName("th")).get(colCounter).text.trim();
					if(colCounter==6)
						expectedtext=expectedtext.replace("\n", "")
				}
				else {
					expectedtext=webDriver.findElements(By.tagName("tr")).get(rowCounter-3).findElements(By.tagName("td")).get(colCounter).text.replace("\n","").trim();
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
		for(int i=0;i<colHeaderWebElements.size()-1;i++) {
			KeywordUtil.logInfo("Verifying ascending order for column : "+colHeaderWebElements.get(i).text)
			while(!colHeaderWebElements.get(i).getAttribute("aria-sort").equals("ascending")) {
				colHeaderWebElements.get(i).click()
				WebUI.delay(1)
			}
			List<WebElement> colWebElements=webDriver.findElements(By.xpath("//td["+(i+1)+"]"))
			for(int j=0;j<colWebElements.size()-1;j++) {
				boolean isFailed=false;
				String firstRow=colWebElements.get(j).text
				String secondRow=colWebElements.get(j+1).text
				if(i==0||i==3||i==4) {
					if(Float.parseFloat(firstRow)>Float.parseFloat(secondRow))
						isFailed=true
				}
				else if(i==2||i==5) {
					if(firstRow.toLowerCase().compareTo(secondRow.toLowerCase())>0)
						isFailed=true
				}
				else
					continue

				if(isFailed)
					KeywordUtil.markFailed("Ascending Soring is failed for column : "+colHeaderWebElements.get(i).text+" at row # "+(j+1)+"/t"+firstRow+"/t"+secondRow)
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
				String firstRow=colWebElements.get(j).text
				String secondRow=colWebElements.get(j+1).text
				if (i==2||i==5) {
					if(firstRow.toLowerCase().compareTo(secondRow.toLowerCase())<0)
						isFailed=true
				}
				else if(i==0||i==3||i==4) {
					if(Float.parseFloat(firstRow)<Float.parseFloat(secondRow))
						isFailed=true
				}
				else
					continue

				if(isFailed)
					KeywordUtil.markFailed("Decending Soring is failed for column : "+colHeaderWebElements.get(i).text+" at row # "+(j+1))
			}
		}
	}
	
	/*
	* Verify Filter
	*/
   @Keyword
   def verifySearch() {
	   KeywordUtil.logInfo("Verifying Search by Lot Number")
	   WebDriver webDriver = DriverFactory.getWebDriver()
	   String input=webDriver.findElement(By.xpath("//td[5]")).text
	   WebUI.sendKeys(findTestObject("Object Repository/Manager Reports/Auto Reports/Car Not Normal/Input_Search"), input)
	   WebUI.click(findTestObject("Object Repository/Manager Reports/Auto Reports/Car Not Normal/Button_Search"))
	   List<WebElement> output=webDriver.findElements(By.xpath("//td[5]"))
	   for(int i=0;i<output.size();i++) {
		   if(!output.get(i).text.equals(input))
			   KeywordUtil.markFailed("Search by Lot Number is failed")
	   }
	   
	   KeywordUtil.logInfo("Verifying Search by VIN")
	   WebUI.clearText(findTestObject("Object Repository/Manager Reports/Auto Reports/Car Not Normal/Input_Search"))
	   WebUI.click(findTestObject("Object Repository/Manager Reports/Auto Reports/Car Not Normal/Button_Search"))
	   input=webDriver.findElement(By.xpath("//td[6]")).text
	   WebUI.sendKeys(findTestObject("Object Repository/Manager Reports/Auto Reports/Car Not Normal/Input_Search"), input)
	   WebUI.click(findTestObject("Object Repository/Manager Reports/Auto Reports/Car Not Normal/Button_Search"))
	   output=webDriver.findElements(By.xpath("//td[6]"))
	   for(int i=0;i<output.size();i++) {
		   if(!output.get(i).text.equals(input))
			   KeywordUtil.markFailed("Search by VIN is failed")
	   }
   }
}
