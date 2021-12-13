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

WebUI.openBrowser(GlobalVariable.URL)

WebUI.maximizeWindow()

WebUI.sendKeys(findTestObject('LoginPage/InputTxt_Email'), GlobalVariable.UserName)

WebUI.sendKeys(findTestObject('LoginPage/InputTxt_Password'), GlobalVariable.Password)

WebUI.click(findTestObject('LoginPage/Btn_Login'))

CustomKeywords.'com.nejoumalijazeera.StatementOfCarsArriving.DismissUnnecessaryNotification'()

WebUI.click(findTestObject('HomePage/Report'))

WebUI.mouseOver(findTestObject('HomePage/Report-Daily Arrived Containers'))

WebUI.scrollToElement(findTestObject('HomePage/Report-Account Report'), 0)

WebUI.click(findTestObject('HomePage/Report-Account Report'))

WebUI.click(findTestObject('HomePage/Account Report - Statement of cars arriving'))

WebUI.selectOptionByIndex(findTestObject('Report/StatementOfCarsArriving/CustomerDropdown'), 3)

WebUI.click(findTestObject('Report/StatementOfCarsArriving/SearchButton'))

WebUI.setText(findTestObject('Report/StatementOfCarsArriving/SearchInputTxt'), text)

WebUI.delay(10)

CustomKeywords.'com.nejoumalijazeera.StatementOfCarsArriving.verifyTextinRows'(text)

