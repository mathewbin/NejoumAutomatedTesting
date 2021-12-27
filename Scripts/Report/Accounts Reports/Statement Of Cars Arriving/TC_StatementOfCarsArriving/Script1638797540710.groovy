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

WebUI.sendKeys(findTestObject('LoginPage/InputTxt_Email'), 'tester@gmail.com')

WebUI.sendKeys(findTestObject('LoginPage/InputTxt_Password'), '88884444333')

WebUI.click(findTestObject('LoginPage/Btn_Login'))

CustomKeywords.'com.nejoumalijazeera.StatementOfCarsArriving.DismissUnnecessaryNotification'()

WebUI.click(findTestObject('HomePage/Report'))

WebUI.mouseOver(findTestObject('HomePage/Report-Daily Arrived Containers'))

WebUI.scrollToElement(findTestObject('HomePage/Report-Account Report'), 0)

WebUI.click(findTestObject('HomePage/Report-Account Report'))

WebUI.click(findTestObject('HomePage/Account Report - Statement of cars arriving'))

WebUI.selectOptionByIndex(findTestObject('Report/StatementOfCarsArriving/CustomerDropdown'), 3)

WebUI.setText(findTestObject('Report/StatementOfCarsArriving/FromDate'), FromDate)

WebUI.setText(findTestObject('Report/StatementOfCarsArriving/ToDate'), ToDate)

WebUI.selectOptionByLabel(findTestObject('Report/StatementOfCarsArriving/StatusDropdown'), 'Paid', false)

WebUI.click(findTestObject('Report/StatementOfCarsArriving/SearchButton'))

CustomKeywords.'com.nejoumalijazeera.StatementOfCarsArriving.verifyRemainingColumnsForPaidStatus'()

WebUI.selectOptionByLabel(findTestObject('Report/StatementOfCarsArriving/StatusDropdown'), 'UnPaid', false)

WebUI.click(findTestObject('Report/StatementOfCarsArriving/SearchButton'))

CustomKeywords.'com.nejoumalijazeera.StatementOfCarsArriving.verifyRemainingColumnsForUnPaidStatus'()

WebUI.click(findTestObject('Report/StatementOfCarsArriving/ExcelButton'))

WebUI.click(findTestObject('Report/StatementOfCarsArriving/CSVButton'))

WebUI.click(findTestObject('Report/StatementOfCarsArriving/PDFButton'))

WebUI.setText(findTestObject('Report/StatementOfCarsArriving/SearchInputTxt'), 'TOYOTA')

WebUI.delay(10)

CustomKeywords.'com.nejoumalijazeera.StatementOfCarsArriving.verifyTextinRows'('TOYOTA')

