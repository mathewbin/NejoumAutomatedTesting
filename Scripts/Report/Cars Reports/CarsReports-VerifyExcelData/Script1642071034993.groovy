import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

WebUI.sendKeys(findTestObject('LoginPage/InputTxt_Email'), GlobalVariable.UserName)

WebUI.sendKeys(findTestObject('LoginPage/InputTxt_Password'), GlobalVariable.Password)

WebUI.click(findTestObject('LoginPage/Btn_Login'))

CustomKeywords.'com.reports.accountreports.CommonUtils.DismissUnnecessaryNotification'()

WebUI.refresh()

WebUI.click(findTestObject('HomePage/Report'))

WebUI.mouseOver(findTestObject('HomePage/Report-Daily Arrived Containers'))

WebUI.scrollToElement(findTestObject('Object Repository/HomePage/Report-Cars Reports'), 0)

WebUI.click(findTestObject('HomePage/Report-Cars Reports'))

WebUI.click(findTestObject('HomePage/Cars Reports-Cars Reports'))

WebUI.click(findTestObject('Report/CarsReports/ShowMoreDetailLink'))

WebUI.selectOptionByValue(findTestObject('Report/CarsReports/DateDropdown'), '1', false)

WebUI.selectOptionByValue(findTestObject('Report/CarsReports/CountryDropdown'), '-1', false)

CustomKeywords.'com.reports.carsreports.CarsReports.selectStatus'(Status)

WebUI.selectOptionByValue(findTestObject('Report/CarsReports/Auction'), '7', false)

WebUI.sendKeys(findTestObject('Report/CarsReports/FromDate'), FromDate)

WebUI.sendKeys(findTestObject('Report/CarsReports/ToDate'), ToDate)

WebUI.click(findTestObject('Report/CarsReports/FilterButton'))

WebUI.delay(5)

CustomKeywords.'com.reports.accountreports.CommonUtils.DismissUnnecessaryNotification'()

CustomKeywords.'com.reports.carsreports.CarsReports.VerifyPurchaseOnFilter'(FromDate, ToDate)

CustomKeywords.'com.reports.carsreports.CarsReports.VerifyStatusFilter'(Status)

CustomKeywords.'com.reports.carsreports.CarsReports.VerifyAuctionFilter'('Copart')

WebUI.selectOptionByValue(findTestObject('Report/CarsReports/DateDropdown'), '2', false)

WebUI.click(findTestObject('Report/CarsReports/FilterButton'))

WebUI.delay(5)

CustomKeywords.'com.reports.accountreports.CommonUtils.DismissUnnecessaryNotification'()

WebUI.selectOptionByValue(findTestObject('Report/CarsReports/ShowDropdown'), '-1', false)

WebUI.delay(2)

WebUI.click(findTestObject('Report/CarsReports/ExcelButton'))

WebUI.delay(5)

CustomKeywords.'com.reports.carsreports.CarsReports.verifyTextFileData'([('FileName') : 'CarBike Report Details.xlsx', ('Date') : 'Created Date'
        , ('Status') : Status, ('ToDate') : ToDate, ('FromDate') : FromDate, ('Country') : 'All', ('Auction') : 'Copart'])

