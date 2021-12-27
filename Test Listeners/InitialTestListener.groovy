import org.apache.commons.io.FileUtils
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory

import internal.GlobalVariable

class InitialTestListener {
	/**
	 * Executes before every test case starts.
	 * @param testCaseContext related information of the executed test case.
	 */
	@BeforeTestCase
	def sampleBeforeTestCase(TestCaseContext testCaseContext) {
			//Set Browser Capabilities.
		KeywordUtil.logInfo("Verifying sampleBeforeTestCase")
	String ProjectDirectory=RunConfiguration.getProjectDir()
	String DownloadFolderPath=ProjectDirectory+"/Download/"
	DownloadFolderPath=DownloadFolderPath.replace('/', '\\')
	FileUtils.cleanDirectory(new File(DownloadFolderPath));
	String AppURL=GlobalVariable.URL
				
	Map<String, Object> chromePrefs = new HashMap<String, Object>()
	chromePrefs.put("download.default_directory", DownloadFolderPath)
	chromePrefs.put("download.prompt_for_download", false)
	ChromeOptions options=new ChromeOptions();
	options.setExperimentalOption("prefs", chromePrefs)

	System.setProperty("webdriver.chrome.driver", DriverFactory.getChromeDriverPath())
	WebDriver driver = new ChromeDriver(options);
	driver.get(AppURL)
	driver.manage().window().maximize()
	DriverFactory.changeWebDriver(driver)
	}
	
	@AfterTestCase
	def sampleAfterTestCase(TestCaseContext testCaseContext) {
		DriverFactory.closeWebDriver();
	}
}