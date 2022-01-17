package com.managerreports.salesreports

import java.text.NumberFormat
import java.text.SimpleDateFormat
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
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI




public class CarSoldReport {

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
				if(i==7){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss yyyy-mm-dd",Locale.ENGLISH);
					if(sdf.parse(firstRow)>sdf.parse(secondRow))
						isFailed=true
				}
				else if(i==0||i==4) {
					if(Float.parseFloat(firstRow)>Float.parseFloat(secondRow))
						isFailed=true
				}
				else {
					if(firstRow.toLowerCase().compareTo(secondRow.toLowerCase())>0)
						isFailed=true
				}

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
				if(i==7) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss yyyy-mm-dd",Locale.ENGLISH);
					if(sdf.parse(firstRow)>sdf.parse(secondRow))
						isFailed=true
				}
				else if (i==0||i==4) {
					if(Float.parseFloat(firstRow)<Float.parseFloat(secondRow))
						isFailed=true
				}
				else {
					if(firstRow.toLowerCase().compareTo(secondRow.toLowerCase())<0)
						isFailed=true
				}

				if(isFailed)
					KeywordUtil.markFailed("Decending Soring is failed for column : "+colHeaderWebElements.get(i).text+" at row # "+(j+1))
			}
		}
	}

	/**
	 * Verify pagination
	 *
	 */
	@Keyword
	def verifyPagination(List<TestObject> PageButtons) {
		KeywordUtil.logInfo("Verifying Pagination")
		WebDriver webDriver = DriverFactory.getWebDriver()
		int totalRows=Integer.parseInt(webDriver.findElement(By.id("carSummary_info")).text.split(" ")[5])
		WebElement previousButton=WebUI.findWebElement(PageButtons.get(1));
		WebElement nextButton=WebUI.findWebElement(PageButtons.get(0));
		if(!previousButton.getAttribute("class").contains("disabled"))
			KeywordUtil.markFailed("Previous button is enable")
		int counter=10
		while(counter<totalRows) {
			nextButton.click()
			nextButton=WebUI.findWebElement(PageButtons.get(0));
			WebUI.delay(1)
			counter+=10
		}
		if(!nextButton.getAttribute("class").contains("disabled"))
			KeywordUtil.markFailed("Next button is enable")
		previousButton=WebUI.findWebElement(PageButtons.get(1));
		while(!previousButton.getAttribute("class").contains("disabled")) {
			previousButton.click()
			WebUI.delay(1)
			previousButton=WebUI.findWebElement(PageButtons.get(1));
		}
	}

	/**
	 * Verify visibility of given text in all rows
	 *
	 */
	@Keyword
	def verifyTextinRows(TestObject searchInputField) {
		KeywordUtil.logInfo("Verifying text in all available rows")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> elements = webDriver.findElements(By.xpath("//td[2]"))
		Random random=new Random()
		int randomNumber=random.nextInt(elements.size())
		String text=elements.get(randomNumber).text.split(" ")[1]
		KeywordUtil.logInfo("Total rows"+elements.size())
		WebUI.sendKeys(searchInputField, text)
		WebUI.delay(2)
		elements = webDriver.findElements(By.xpath("//td[2]"))
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
	 * Verify Customer filter
	 *
	 */
	@Keyword
	def VerifyCustomerFilter(TestObject customerDropdownElement) {
		KeywordUtil.logInfo("Verifying Customer filter")
		WebDriver webDriver = DriverFactory.getWebDriver()
		WebElement element = WebUI.findWebElement(customerDropdownElement);
		Select selectCustomerDropdown=new Select(element)
		String customerName=selectCustomerDropdown.firstSelectedOption.text.split("/")[0]

		List<WebElement> elements = webDriver.findElements(By.xpath("//td[6]"))
		boolean isSuccess=true;
		for(int i=0;i<elements.size();i++) {
			if(!elements[i].text.toLowerCase().contains(customerName.toLowerCase())) {
				KeywordUtil.markFailed("Sold Customer "+customerName+" verification failed. Actual : "+elements[i].text)
				break;
			}
		}
	}

	/**
	 * Verify Sold By filter
	 *
	 */
	@Keyword
	def VerifySoldByFilter(TestObject soldDropdownElement) {
		KeywordUtil.logInfo("Verifying Sold By filter")
		WebDriver webDriver = DriverFactory.getWebDriver()
		WebElement element = WebUI.findWebElement(soldDropdownElement);
		Select selectSoldCustomerDropdown=new Select(element)
		String customerName=selectSoldCustomerDropdown.firstSelectedOption.text.split("/")[0]

		List<WebElement> elements = webDriver.findElements(By.xpath("//td[7]"))
		for(int i=0;i<elements.size();i++) {
			if(!elements[i].text.toLowerCase().contains(customerName.toLowerCase())) {
				KeywordUtil.markFailed("Sold Customer "+customerName+" verification failed. Actual : "+elements[i].text)
				break;
			}
		}
	}

	/**
	 * Verify Created Date Filter
	 */
	@Keyword
	def VerifyCreatedDateFilter(String dateInput1,String dateInput2) {
		KeywordUtil.logInfo("Verifying Created Date Filter")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> webElements=webDriver.findElements(By.xpath("//td[8]"))
		SimpleDateFormat Expectedsdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss yyyy-mm-dd",Locale.ENGLISH);
		SimpleDateFormat inputsdf = new SimpleDateFormat("mm/dd/yyyy",Locale.ENGLISH);
		Date fromDate=inputsdf.parse(dateInput1)
		Date toDate=inputsdf.parse(dateInput2)
		for(int i=0;i<webElements.size();i++) {
			String[] arrActualDateInStr=webElements.get(i).text.split("\n")
			Date actualDate=Expectedsdf.parse(arrActualDateInStr[0]+" "+arrActualDateInStr[1])
			if(actualDate>=fromDate && actualDate<=toDate)
				continue
			else
				KeywordUtil.markError("Created Date "+actualDate+" not in the given range")
		}
	}

	/**
	 * Verify Excel File Data
	 *
	 */
	@Keyword
	def verifyTextFileData(List<TestObject> dropdownElements,Map<String,Object> keys) {
		KeywordUtil.logInfo("Verifying excel file data")
		WebDriver webDriver = DriverFactory.getWebDriver()

		int expTotalColCount=webDriver.findElements(By.tagName("th")).size()
		int expTotalRowCount=webDriver.findElements(By.tagName("tr")).size()-1

		String ProjectDirectory=RunConfiguration.getProjectDir()
		String excelFileLocation=ProjectDirectory+"/Download/"+keys.get("FileName")
		excelFileLocation=excelFileLocation.replace('/', '\\')
		FileInputStream fis=new FileInputStream(new File(excelFileLocation));
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row = sheet.getRow(0);

		KeywordUtil.logInfo("Verifying headers")
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-M-d");
		String value=sheet.getRow(0).getCell(0).getStringCellValue().trim()
		if(!value.equals("Sold car"))
			KeywordUtil.logInfo("Sold car heading is not found. Actual : "+value)
		SimpleDateFormat inputsdf = new SimpleDateFormat("MM/dd/yyyy",Locale.ENGLISH);
		//Date fromDate=inputsdf.parse(dateInput1)
		String expectedValue=keys.get("FromDate")
		value=sheet.getRow(1).getCell(0).getStringCellValue().trim()
		expectedValue="From : "+inputsdf.parse(expectedValue).format("yyyy-MM-dd")
		if(!value.equals(expectedValue))
			KeywordUtil.logInfo("From Date "+expectedValue+" is not found. Actual : "+value)
		expectedValue="To : "+inputsdf.parse(keys.get("ToDate")).format("yyyy-MM-dd")
		value=sheet.getRow(2).getCell(0).getStringCellValue().trim()
		if(!value.equals(expectedValue))
			KeywordUtil.logInfo("To Date "+expectedValue+" is not found. Actual : "+value)
		Select select1 = new Select(WebUI.findWebElement(dropdownElements.get(1)))
		expectedValue="Customer :  "+select1.getFirstSelectedOption().text.split("/")[0]
		value=sheet.getRow(1).getCell(1).getStringCellValue().trim().split("/")[0]
		if(!value.equals(expectedValue))
			KeywordUtil.logInfo("Customer "+expectedValue+" is not found. Actual : "+value)
		Select select2 = new Select(WebUI.findWebElement(dropdownElements.get(0)))
		expectedValue="Sold By: "+select2.getFirstSelectedOption().text
		value=sheet.getRow(2).getCell(1).getStringCellValue().trim()
		LocalDateTime now = LocalDateTime.now()
		expectedValue="Exported Date : "+dtf.format(now)
		value=sheet.getRow(1).getCell(2).getStringCellValue().trim()
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
					/*if(rowCounter>2 && (colCounter==3||colCounter==4||colCounter==5))
					 actualText=actualText.replace(" ", "").trim()
					 if(colCounter==1)
					 actualText=actualText.replace("  ", " ").trim()*/
						break;
					case Cell.CELL_TYPE_NUMERIC:
						double d=  cell.getNumericCellValue()
						actualText=d<1000?((int)d).toString():d.toString()
						break;
				}
				if(rowCounter==3) {
					expectedtext=webDriver.findElement(By.tagName("tr")).findElements(By.tagName("th")).get(colCounter).text.trim();
				}
				else {
					expectedtext=webDriver.findElements(By.tagName("tr")).get(rowCounter-3).findElements(By.tagName("td")).get(colCounter).text.replace("\n","").trim();
					if(colCounter==0||colCounter==4) {
						double d= Double.parseDouble(expectedtext)
						expectedtext=d<1000?((int)d).toString():d.toString()
					}
				}
				if(!expectedtext.replace(" ", "").equals(actualText.replace(" ", "")))
					KeywordUtil.logInfo("Data is not matched in excel sheet. Expected : "+expectedtext+" Actual : "+actualText+" Index : ["+rowCounter+","+colCounter+"]");
				colCounter++;
			}
			KeywordUtil.logInfo("Row # "+(rowCounter+1)+" is verified successfully")
			rowCounter++;
		}
	}


}
