package com.managerreports.autoreports

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




public class CarMissingPrices {

	/**
	 * Verify visibility of given text in all rows
	 *
	 */
	@Keyword
	def verifyTextinRows(TestObject searchInputField) {
		KeywordUtil.logInfo("Verifying text in all available rows")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> elements = webDriver.findElements(By.xpath("//td[3]"))
		Random random=new Random()
		int randomNumber=random.nextInt(elements.size())
		String text=elements.get(randomNumber).text.split(" ")[0]
		KeywordUtil.logInfo("Total rows"+elements.size())
		WebUI.sendKeys(searchInputField, text)
		WebUI.delay(2)
		elements = webDriver.findElements(By.xpath("//td[3]"))
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
				if(i==1||i==2) {
					if(firstRow.toLowerCase().compareTo(secondRow.toLowerCase())>0)
						isFailed=true
				}
				else {
					if(Float.parseFloat(firstRow)>Float.parseFloat(secondRow))
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
				if (i==1||i==2) {
					if(firstRow.toLowerCase().compareTo(secondRow.toLowerCase())<0)
						isFailed=true
				}
				else {
					if(Float.parseFloat(firstRow)<Float.parseFloat(secondRow))
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
		int totalRows=Integer.parseInt(webDriver.findElement(By.id("tableLengthAndBTN_info")).text.split(" ")[5])
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
	 * Verify Excel File Data
	 *
	 */
	@Keyword
	def verifyTextFileData(TestObject statusDropdownElements,String fileName) {
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

		KeywordUtil.logInfo("Verifying headers")
		String value=sheet.getRow(0).getCell(4).getStringCellValue().trim()
		if(!value.equals("Cars with missing prices"))
			KeywordUtil.logInfo("Cars with missing prices heading is not found. Actual : "+value)
		Select select = new Select(WebUI.findWebElement(statusDropdownElements))
		String expectedValue="Status: "+select.getFirstSelectedOption().text
		value=sheet.getRow(2).getCell(4).getStringCellValue()
		if(!value.equals(expectedValue))
			KeywordUtil.logInfo("Status: "+expectedValue+" is not found. Actual : "+value)
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
			if(rowCounter<3) {
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
				if(rowCounter==3) {
					expectedtext=webDriver.findElement(By.tagName("tr")).findElements(By.tagName("th")).get(colCounter).text.trim();
				}
				else {
					expectedtext=webDriver.findElements(By.tagName("tr")).get(rowCounter-3).findElements(By.tagName("td")).get(colCounter).text.replace("\n","").trim();
					if(colCounter!=1 && colCounter!=2) {
						double d= Double.parseDouble(expectedtext)
						expectedtext=d<1000?((int)d).toString():d.toString()
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
	 * Verify Filter
	 */
	@Keyword
	def verifyFilter(List<TestObject> testObjects) {
		KeywordUtil.logInfo("Verifying Filter")
		WebDriver webDriver = DriverFactory.getWebDriver()
		String xpath="//td[var]"
		for(int i=1;i<=5;i++) {
			WebUI.selectOptionByIndex(testObjects.get(0), i)
			WebUI.click(testObjects.get(1))
			WebUI.delay(2)
			List<WebElement> cols=webDriver.findElements(By.xpath(xpath.replace("var",""+(3+i))))
			for(int j=0;j<cols.size();j++) {
				if(Float.parseFloat(cols.get(j).text)!=0)
					KeywordUtil.markFailed("Filter failed for index "+i+"\t"+j+"\t"+cols.get(j).text)
			}
		}
	}
}
