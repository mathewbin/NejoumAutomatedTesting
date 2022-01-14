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



public class CarsReports {

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
				if(i==2||i==5||i==7||i>8) {
					if(firstRow.toLowerCase().compareTo(secondRow.toLowerCase())>0)
						isFailed=true

				}
				else if(i==6){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd",Locale.ENGLISH);
					if(sdf.parse(firstRow)>sdf.parse(secondRow))
						isFailed=true
				}
				else if(i==0) {
					if(Float.parseFloat(firstRow)>Float.parseFloat(secondRow))
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
				if(i==2||i==5||i==7||i>8) {
					if(firstRow.toLowerCase().compareTo(secondRow.toLowerCase())<0)
						isFailed=true
				}
				else if(i==6){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd",Locale.ENGLISH);
					if(sdf.parse(firstRow)<sdf.parse(secondRow))
						isFailed=true
				}
				else if (i==0) {
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

	/**
	 * Verify Show Entries
	 */
	@Keyword
	def verifyShowEntry(TestObject dropdownObject,int rowCount) {
		KeywordUtil.logInfo("Verifying row count as per selection")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> elements = webDriver.findElements(By.xpath("//td/parent::tr"))
		WebElement element = WebUI.findWebElement(dropdownObject);
		Select selectDropdown=new Select(element);
		if(Integer.parseInt(selectDropdown.firstSelectedOption.text)!=rowCount)
			KeywordUtil.markFailed("Expected row count "+rowCount+" is not matched. Actual : "+selectDropdown.firstSelectedOption.text)
		else if(elements.size()!=rowCount)
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
	 * Verify pagination
	 *
	 */
	@Keyword
	def verifyPagination(TestObject showDropdown, TestObject previousButtonObject, TestObject nextButtonObject) {
		KeywordUtil.logInfo("Verifying Pagination")
		WebDriver webDriver = DriverFactory.getWebDriver()
		WebElement selectedDropdownValue = WebUI.findWebElement(showDropdown);
		Select selectDropdown=new Select(selectedDropdownValue);
		int perPageEntry=Integer.parseInt(selectDropdown.firstSelectedOption.text)
		int totalRows=Integer.parseInt(webDriver.findElement(By.id("tableLengthAndBTN_info")).text.split(" ")[5])
		WebElement previousButton=WebUI.findWebElement(previousButtonObject);
		WebElement nextButton=WebUI.findWebElement(nextButtonObject);
		if(!previousButton.getAttribute("class").contains("disabled"))
			KeywordUtil.markFailed("Previous button is enable")
		int counter=perPageEntry>totalRows?totalRows:perPageEntry
		while(counter<totalRows) {
			nextButton.click()
			nextButton=WebUI.findWebElement(nextButtonObject);
			WebUI.delay(1)
			counter+=perPageEntry
		}
		if(!nextButton.getAttribute("class").contains("disabled"))
			KeywordUtil.markFailed("Next button is enable")
		previousButton=WebUI.findWebElement(previousButtonObject);
		while(!previousButton.getAttribute("class").contains("disabled")) {
			previousButton.click()
			WebUI.delay(1)
			previousButton=WebUI.findWebElement(previousButtonObject);
		}
	}

	/**
	 * Select Status
	 *
	 */
	@Keyword
	def selectStatus(String status) {
		KeywordUtil.logInfo("Selecting Status")
		WebDriver webDriver = DriverFactory.getWebDriver()
		WebUI.sendKeys(findTestObject("Report/CarsReports/All Cars Reports/StatusDropdown"), status)
		webDriver.findElement(By.xpath("//li[text()='"+status+"']")).click()
	}

	/**
	 * Verify Purchase on Filter
	 */
	@Keyword
	def VerifyPurchaseOnFilter(String dateInput1,String dateInput2) {
		KeywordUtil.logInfo("Verifying Purchase Filter")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> webElements=webDriver.findElements(By.xpath("//td[7]"))
		SimpleDateFormat Expectedsdf = new SimpleDateFormat("yyyy-mm-dd",Locale.ENGLISH);
		SimpleDateFormat inputsdf = new SimpleDateFormat("mm/dd/yyyy",Locale.ENGLISH);
		Date fromDate=inputsdf.parse(dateInput1)
		Date toDate=inputsdf.parse(dateInput2)
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
	 * Verify Created Date Filter
	 */
	@Keyword
	def VerifyCreatedDateFilter(String dateInput1,String dateInput2) {
		KeywordUtil.logInfo("Verifying Created Date Filter")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> webElements=webDriver.findElements(By.xpath("//td[12]"))
		SimpleDateFormat Expectedsdf = new SimpleDateFormat("yyyy-mm-dd",Locale.ENGLISH);
		SimpleDateFormat inputsdf = new SimpleDateFormat("mm/dd/yyyy",Locale.ENGLISH);
		Date fromDate=inputsdf.parse(dateInput1)
		Date toDate=inputsdf.parse(dateInput2)
		for(int i=0;i<webElements.size();i++) {
			// Converting Array to List
			String[] arr=webElements.get(i).text.split(" ")
			List<String> list = Arrays.asList(arr);
			// Reversing the list using Collections.reverse() method
			Collections.reverse(list);
			KeywordUtil.logInfo("Actual Data in Str : "+list.get(1).split("\n")[1])
			Date actualDate=Expectedsdf.parse(list.get(1).split("\n")[1])
			if(actualDate>=fromDate && actualDate<=toDate)
				continue
			else
				KeywordUtil.markError("Created Date "+actualDate+" not in the given range")
		}
	}

	/**
	 * Verify Status Filter
	 */
	@Keyword
	def VerifyStatusFilter(String status) {
		KeywordUtil.logInfo("Verifying Status Filter")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> webElements=webDriver.findElements(By.xpath("//td[10]"))
		for(int i=0;i<webElements.size();i++) {
			String actualStatus=webElements.get(i).text
			if(!actualStatus.equals(status))
				KeywordUtil.markError("Status "+status+" not found in row "+(i+1)+" Actual : "+actualStatus)
		}
	}

	/**
	 * Verify Auction Filter
	 */
	@Keyword
	def VerifyAuctionFilter(String auction) {
		KeywordUtil.logInfo("Verifying Auction Filter")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> webElements=webDriver.findElements(By.xpath("//td[6]"))
		for(int i=0;i<webElements.size();i++) {
			String actualauction=webElements.get(i).text
			if(!actualauction.contains(auction))
				KeywordUtil.markError("Auction "+auction+" not found in row "+(i+1)+" Actual : "+actualauction)
		}
	}

	/**
	 * Verify Excel File Data
	 *
	 */
	@Keyword
	def verifyTextFileData(Map<String,String> keys) {
		KeywordUtil.logInfo("Verifying excel file data")
		WebDriver webDriver = DriverFactory.getWebDriver()
		NumberFormat formatter = NumberFormat.getCurrencyInstance();

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
		String value=sheet.getRow(0).getCell(4).getStringCellValue().trim()
		if(!value.equals("Cars Reports"))
			KeywordUtil.markError("Cars Reports heading is not found. Actual : "+value)
		String expectedValue=keys.get("Date")
		value=sheet.getRow(2).getCell(0).getStringCellValue().trim()
		expectedValue="Date : "+expectedValue
		if(!value.equals(expectedValue))
			KeywordUtil.markError("Date "+expectedValue+" is not found. Actual : "+value)
		expectedValue=keys.get("FromDate")
		value=sheet.getRow(2).getCell(1).getStringCellValue().trim()
		expectedValue="From: "+expectedValue
		if(!value.equals(expectedValue))
			KeywordUtil.markError("From Date "+expectedValue+" is not found. Actual : "+value)
		expectedValue="To: "+keys.get("ToDate")
		value=sheet.getRow(3).getCell(1).getStringCellValue().trim()
		if(!value.equals(expectedValue))
			KeywordUtil.markError("To Date "+expectedValue+" is not found. Actual : "+value)
		expectedValue="Auction : "+keys.get("Auction")
		value=sheet.getRow(3).getCell(2).getStringCellValue().trim()
		if(!value.equals(expectedValue))
			KeywordUtil.markError("Auction "+expectedValue+" is not found. Actual : "+value)
		expectedValue="Status : "+keys.get("Status")
		value=sheet.getRow(2).getCell(4).getStringCellValue().trim()
		if(!value.equals(expectedValue))
			KeywordUtil.markError("Status "+expectedValue+" is not found. Actual : "+value)
		expectedValue="Country : "+keys.get("Country")
		value=sheet.getRow(3).getCell(4).getStringCellValue().trim()
		if(!value.equals(expectedValue))
			KeywordUtil.markError("Country "+expectedValue+" is not found. Actual : "+value)
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-M-d");
		LocalDateTime now = LocalDateTime.now()
		expectedValue="Exported Date : "+dtf.format(now)
		value=sheet.getRow(1).getCell(4).getStringCellValue().trim()
		if(!value.equals(expectedValue))
			KeywordUtil.markError("Exported Date "+expectedValue+" is not found. Actual : "+value)

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
				if(colCounter==1) {
					colCounter++
					continue
				}
				String expectedtext="";
				String actualText="";
				switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						actualText=cell.getStringCellValue()
						if(rowCounter>4 && (colCounter==3||colCounter==4||colCounter==5))
							actualText=actualText.replace(" ", "").trim()
						if(colCounter==10)
							actualText=actualText.replace("  ", " ").trim()
						break;
					case Cell.CELL_TYPE_NUMERIC:
						double d=  cell.getNumericCellValue()
						actualText=d<1000?((int)d).toString():formatter.format(d)
						break;
				}
				if(rowCounter==4) {
					expectedtext=webDriver.findElement(By.tagName("tr")).findElements(By.tagName("th")).get(colCounter).text.trim();
				}
				else {
					expectedtext=webDriver.findElements(By.tagName("tr")).get(rowCounter-4).findElements(By.tagName("td")).get(colCounter).text.replace("\n","").trim();
					if(colCounter==3||colCounter==4||colCounter==5)
						expectedtext=expectedtext.replace(" ", "").trim()
				}
				if(!expectedtext.equals(actualText))
					KeywordUtil.markError("Data is not matched in excel sheet. Expected : "+expectedtext+" Actual : "+actualText+" Index : ["+rowCounter+","+colCounter+"]");
				colCounter++;
			}
			KeywordUtil.logInfo("Row # "+(rowCounter+1)+" is verified successfully")
			rowCounter++;
		}
	}

}
