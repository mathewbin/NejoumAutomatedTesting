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

WebUI.click(findTestObject('HomePage/Manager Reports'))

WebUI.click(findTestObject('Manager Reports/Car Reports/Car Sold Report'))

WebUI.delay(5)

CustomKeywords.'com.reports.accountreports.CommonUtils.DismissUnnecessaryNotification'()

CustomKeywords.'com.managerreports.salesreports.CarSoldReport.verifyTextinRows'(findTestObject('Manager Reports/Sales Reports/Car Sold Report/SearchInputTxt'))

CustomKeywords.'com.managerreports.salesreports.CarSoldReport.VerifyingOrder'()

CustomKeywords.'com.managerreports.salesreports.CarSoldReport.verifyPagination'([findTestObject('Manager Reports/Sales Reports/Car Sold Report/NextButton')
        , findTestObject('Manager Reports/Sales Reports/Car Sold Report/PreviousButton')])

