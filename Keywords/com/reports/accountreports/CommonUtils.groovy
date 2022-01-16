package com.reports.accountreports

import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory













public class CommonUtils {

	/**
	 * Verify File Existence
	 * @param table File Name
	 */
	@Keyword
	def void VerifyFileDownloaded(String fileName) {
		String ProjectDirectory=RunConfiguration.getProjectDir()
		String DownloadFolderPath=ProjectDirectory+"/Download/"
		DownloadFolderPath=DownloadFolderPath.replace('/', '\\')
		if(!new File(DownloadFolderPath+fileName).exists())
			KeywordUtil.markFailed("File "+fileName+" is not downloaded")
	}

	/**
	 * Dismiss unnecessary notication
	 */
	@Keyword
	def DismissUnnecessaryNotification() {
		KeywordUtil.logInfo("Dismissing unnecessary notification")
		WebDriver webDriver = DriverFactory.getWebDriver()

		webDriver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE)
		while(webDriver.findElements(By.cssSelector("button.swal2-confirm")).size()>0) {
			webDriver.findElement(By.cssSelector("button.swal2-confirm")).click()
			KeywordUtil.logInfo("Dismissed notification")
			Thread.sleep(2000)
		}
	}
}
