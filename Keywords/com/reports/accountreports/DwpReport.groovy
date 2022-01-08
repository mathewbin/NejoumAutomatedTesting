package com.reports.accountreports

import java.text.NumberFormat
import java.text.SimpleDateFormat

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
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI







public class DwpReport {

	/**
	 * Verify Show Entries
	 */
	@Keyword
	def verifyShowEntry(TestObject showDropdown, String rowCount) {
		KeywordUtil.logInfo("Verifying row count as per selection")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> elements = webDriver.findElements(By.xpath("//td/parent::tr"))
		WebElement element = WebUI.findWebElement(showDropdown);
		Select selectDropdown=new Select(element);
		if(!selectDropdown.firstSelectedOption.text.equals(rowCount))
			KeywordUtil.markFailed("Expected row count is not matched.")
		else if(elements.size()!=Integer.parseInt(rowCount))
			KeywordUtil.markPassed("Displayed Row count is not matched as per selection")
		else
			KeywordUtil.logInfo("Show Entry Verified for Row Count "+rowCount);
	}

	/**
	 * Verify TestData
	 */
	@Keyword
	def verifyRowData(TestData testdata) {
		int expectedRowNumbers=testdata.getRowNumbers()
		int expectedColNumbers=testdata.getColumnNumbers()
		WebDriver webDriver = DriverFactory.getWebDriver()
		//Verifying Row Count
		String temp=webDriver.findElement(By.id("example23_info")).getText().replace("Showing 1 to 200 of ", "").replace(" entries", "").replace(",", "");
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
				String expectedData=testdata.getValue(j, i).trim()
				expectedData=expectedData.isEmpty()?"/":expectedData
				actualData=webDriver.findElement(By.xpath("//tr["+i+"]/td["+(j+1)+"]")).getText().trim()
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
	 * Verify visibility of given text in all rows
	 *
	 */
	@Keyword
	def verifyTextinRows(String text) {
		KeywordUtil.logInfo("Verifying text in all available rows")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> elements = webDriver.findElements(By.xpath("//td/parent::tr"));
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
	 * Verify Excel File Data
	 *
	 */
	@Keyword
	def verifyTextFileData(String fileName) {
		KeywordUtil.logInfo("Verifying excel file data")
		WebDriver webDriver = DriverFactory.getWebDriver()
		NumberFormat formatter = NumberFormat.getCurrencyInstance();

		int expTotalColCount=webDriver.findElements(By.tagName("th")).size()
		int expTotalRowCount=webDriver.findElements(By.tagName("tr")).size()

		String ProjectDirectory=RunConfiguration.getProjectDir()
		String excelFileLocation=ProjectDirectory+"/Download/"+fileName
		excelFileLocation=excelFileLocation.replace('/', '\\')
		FileInputStream fis=new FileInputStream(new File(excelFileLocation));
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet = wb.getSheetAt(0);
		int actTotalRow=sheet.getLastRowNum()+1;
		if(expTotalRowCount!=actTotalRow)
			KeywordUtil.markFailed("Row count is not matched in Excel sheet. Actual : "+actTotalRow+" Expected : "+expTotalRowCount);
		XSSFRow row = sheet.getRow(0);
		int actTotalColCount=row.getLastCellNum();
		if(expTotalColCount!=actTotalColCount)
			KeywordUtil.markFailed("Column count is not matched in Excel sheet. Actual : "+actTotalColCount+" Expected : "+expTotalColCount);
		Iterator<Row> itr = sheet.iterator();
		int rowCounter=0;
		while (itr.hasNext()) {
			int colCounter=0;
			row = itr.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				String expectedtext="";
				String actualText="";
				switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						actualText=cell.getStringCellValue().trim()
						break;
					case Cell.CELL_TYPE_NUMERIC:
						double d=  cell.getNumericCellValue()
						actualText=d<1000?((int)d).toString():formatter.format(d)
						break;
				}
				if(rowCounter==0) {
					expectedtext=webDriver.findElement(By.tagName("tr")).findElements(By.tagName("th")).get(colCounter).text.trim();
				}
				else {
					expectedtext=webDriver.findElements(By.tagName("tr")).get(rowCounter).findElements(By.tagName("td")).get(colCounter).text.replace("\n","").trim();
					if(!expectedtext.contains(",") && (colCounter==0 || (colCounter>3 && colCounter<25))) {
						float d=  Float.parseFloat(expectedtext)
						expectedtext=d<1000?((int)d).toString():d
					}
				}
				if(!expectedtext.equals(actualText))
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
				if(i==1) {
					if(firstRow.compareTo(secondRow)>0)
						isFailed=true

				}
				else if(i==4){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss",Locale.ENGLISH);
					if(sdf.parse(firstRow)>sdf.parse(secondRow))
						isFailed=true
				}
				else {
					if(Float.parseFloat(firstRow)>Float.parseFloat(secondRow))
						isFailed=true
				}
				if(isFailed)
					KeywordUtil.markFailed("Ascending Soring is failed for column : "+colHeaderWebElements.get(i).text+" at row # "+(j+1))
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
				if(i==1) {
					if(firstRow.compareTo(secondRow)<0)
						isFailed=true
				}
				else if(i==4){
					if(Date.parseToStringDate(firstRow)<Date.parseToStringDate(secondRow))
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
}


