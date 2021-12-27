package com.nejoumalijazeera

import java.awt.geom.Arc2D.Double
import java.text.NumberFormat

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory




class StatementOfCarsArriving {
	/**
	 * Verify amount 0.00 for Remaining column for Paid status 
	 */
	@Keyword
	def verifyRemainingColumnsForPaidStatus() {
		KeywordUtil.logInfo("Verifying rows for Paid status")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> elements = webDriver.findElements(By.xpath("//td[23]"))
		KeywordUtil.logInfo("Total rows"+elements.size())
		boolean isSuccess=true;
		for(int i=0;i<elements.size();i++) {
			if(!elements[i].text.equals("0.00")) {
				isSuccess=false;
				break;
			}
		}
		if(isSuccess)
			KeywordUtil.markPassed("Remaining column value is verified successfully")
		else
			KeywordUtil.markFailed("Remaining column is non-zero")
	}

	/**
	 * Verify Total for all columns
	 */
	@Keyword
	def verifyColumnsTotal() {
		KeywordUtil.logInfo("Verifying total")
		String[] columns=[
			"Towing",
			"Shipping",
			"Clearance",
			"Custom",
			"Extra",
			"Additional",
			"Recovery",
			"Towing Fines",
			"Vat",
			"Shipping Commission",
			"Discount",
			"BOS",
			"Storage Old",
			"Other",
			"Total",
			"Storage New",
			"Forklift",
			"Paid",
			"Remaining",
			"Car Remaining",
			"Final"
		]
		NumberFormat format = NumberFormat.getCurrencyInstance();
		WebDriver webDriver = DriverFactory.getWebDriver()
		Number number =null;
		for(int i=5;i<=25;i++) {
			List<WebElement> elements = webDriver.findElements(By.xpath("//td["+i+"]"))
			Float sum=0.00;
			for(int j=0;j<elements.size()-1;j++) {
				sum=sum+Float.parseFloat(elements[j].getText().replace(",",""))
			}
			if(!Float.parseFloat(elements[elements.size()-1].getText().replace(",","")).equals(Float.parseFloat(sum.toString()))) {
				KeywordUtil.markFailed("Total is not matched for column "+columns[i-5]+". Actual : "+elements[elements.size()-1].getText()+" Expected : "+sum)
			}
			else {
				KeywordUtil.logInfo("Total "+elements[elements.size()-1].getText()+" is matched for Column : "+columns[i-5]);
			}
		}
	}

	/**
	 * Verify amount 0.00 for Remaining column for Paid status
	 */
	@Keyword
	def verifyRemainingColumnsForUnPaidStatus() {
		KeywordUtil.logInfo("Verifying rows for UnPaid status")
		WebDriver webDriver = DriverFactory.getWebDriver()
		List<WebElement> elements = webDriver.findElements(By.xpath("//td[23]"))
		KeywordUtil.logInfo("Total rows"+elements.size())
		boolean isSuccess=true;
		for(int i=0;i<elements.size();i++) {
			if(elements[i].text.equals("0.00")) {
				isSuccess=false;
				break;
			}
		}
		if(isSuccess)
			KeywordUtil.markPassed("Remaining column value is verified successfully")
		else
			KeywordUtil.markFailed("Remaining column is zero")
	}

	/**
	 * Dismiss unnecessary notication  
	 */
	@Keyword
	def DismissUnnecessaryNotification() {
		KeywordUtil.logInfo("Dismissing unnecessary notification")
		WebDriver webDriver = DriverFactory.getWebDriver()

		webDriver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE)
		while(webDriver.findElements(By.xpath("//button[text()='OK']")).size()>0) {
			webDriver.findElement(By.xpath("//button[text()='OK']")).click()
			Thread.sleep(2000)
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
		List<WebElement> elements = webDriver.findElements(By.xpath("//tr[not(position() > last() -1)]"));
		KeywordUtil.logInfo("Total rows"+elements.size())
		boolean isSuccess=true;
		for(int i=0;i<elements.size();i++) {
			if(!elements[i].text.toLowerCase().contains(text.toLowerCase())) {
				isSuccess=false;
				break;
			}
		}
		if(isSuccess)
			KeywordUtil.markPassed("Remaining column value is verified successfully")
		else
			KeywordUtil.markFailed("Remaining column is non-zero")
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
						actualText=d<1000?((int)d).toString():formatter.format(d).replace("\$", "")
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
}