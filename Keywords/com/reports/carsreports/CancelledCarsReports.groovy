package com.reports.carsreports

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
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



public class CancelledCarsReports {

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
				if(i==1||i==3||i==9||i==10||i==12) {
					continue
				}
				else if(i==8){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd",Locale.ENGLISH);
					if(sdf.parse(firstRow)>sdf.parse(secondRow))
						isFailed=true
				}
				else if(i==14){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss",Locale.ENGLISH);
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
				if(i==1||i==3||i==9||i==10||i==12) {
					continue
				}
				else if(i==8){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd",Locale.ENGLISH);
					if(sdf.parse(firstRow)<sdf.parse(secondRow))
						isFailed=true
				}
				else if(i==14){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss",Locale.ENGLISH);
					if(sdf.parse(firstRow)<sdf.parse(secondRow))
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
	 * Verify visibility of given text in all rows
	 *
	 */
	@Keyword
	def verifyTextinRows() {
		KeywordUtil.logInfo("Verifying text in all available rows")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> elements = webDriver.findElements(By.xpath("//td[4]"));
		Random random=new Random()
		int randomNumber=random.nextInt(elements.size())
		String text=elements.get(randomNumber).text.split(" ")[0]
		KeywordUtil.logInfo("Total rows"+elements.size())
		WebUI.sendKeys(findTestObject("Report/CarsReports/All Cars Reports/SearchInputTxt"), text)
		WebUI.delay(2)
		elements = webDriver.findElements(By.xpath("//td[4]"));
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
	 * Verify Auction Filter
	 */
	@Keyword
	def VerifyAuctionFilter(String auction) {
		KeywordUtil.logInfo("Verifying Auction Filter")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> webElements=webDriver.findElements(By.xpath("//td[7]"))
		for(int i=0;i<webElements.size();i++) {
			String actualauction=webElements.get(i).text
			if(!actualauction.contains(auction))
				KeywordUtil.markError("Auction "+auction+" not found in row "+(i+1)+" Actual : "+actualauction)
		}
	}

	/**
	 * Verify Purchase on Filter
	 */
	@Keyword
	def VerifyPurchaseOnFilter(List<String> dateInput) {
		KeywordUtil.logInfo("Verifying Purchase Filter")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> webElements=webDriver.findElements(By.xpath("//td[9]"))
		SimpleDateFormat Expectedsdf = new SimpleDateFormat("yyyy-mm-dd",Locale.ENGLISH);
		SimpleDateFormat inputsdf = new SimpleDateFormat("mm/dd/yyyy",Locale.ENGLISH);
		Date fromDate=inputsdf.parse(dateInput.get(0))
		Date toDate=inputsdf.parse(dateInput.get(1))
		for(int i=0;i<webElements.size();i++) {
			String actualDateInStr=webElements.get(i).text
			Date actualDate=Expectedsdf.parse(actualDateInStr)
			if(actualDate>=fromDate && actualDate<=toDate)
				continue
			else
				KeywordUtil.markError("Purchase Date "+actualDate+" not in the given range")
		}
	}

	/**
	 * Verify Excel File Data
	 *
	 */
	@Keyword
	def verifyTextFileData(String fileName) {
		KeywordUtil.logInfo("Verifying excel file data")
		WebDriver webDriver = DriverFactory.getWebDriver()

		int expTotalColCount=webDriver.findElements(By.tagName("th")).size()
		int expTotalRowCount=webDriver.findElements(By.tagName("tr")).size()-1

		String ProjectDirectory=RunConfiguration.getProjectDir()
		String excelFileLocation=ProjectDirectory+"/Download/"+fileName
		excelFileLocation=excelFileLocation.replace('/', '\\')
		FileInputStream fis=new FileInputStream(new File(excelFileLocation));
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row = sheet.getRow(0);


		Iterator<Row> itr = sheet.iterator();
		int rowCounter=0;
		while (itr.hasNext()) {
			row = itr.next();
			int colCounter=0;
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				if(colCounter==1) {
					colCounter++
					continue
				}
				String expectedtext="";
				String actualText="";
				switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						actualText=cell.getStringCellValue()
						if(colCounter==3)
							actualText=actualText.replace(" ", "").trim()
						if(colCounter==10)
							actualText=actualText.replace("  ", " ").trim()
						break;
					case Cell.CELL_TYPE_NUMERIC:
						double d=  cell.getNumericCellValue()
						actualText=((int)d).toString()
						break;
				}
				if(rowCounter==0) {
					expectedtext=webDriver.findElement(By.tagName("tr")).findElements(By.tagName("th")).get(colCounter).text.trim();
				}
				else {
					expectedtext=webDriver.findElements(By.tagName("tr")).get(rowCounter).findElements(By.tagName("td")).get(colCounter).text.replace("\n","").trim();
					if(colCounter==3)
						expectedtext=expectedtext.replace(" ", "").trim()
				}
				if(!expectedtext.equals(actualText))
					KeywordUtil.markFailed("Data is not matched in excel sheet. Expected : "+expectedtext+" Actual : "+actualText+" Index : ["+rowCounter+","+colCounter+"]");
				colCounter++;
			}
			KeywordUtil.logInfo("Row # "+(rowCounter+1)+" is verified successfully")
			rowCounter++;
		}
	}

}
