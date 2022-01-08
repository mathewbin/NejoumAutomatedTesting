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

WebUI.click(findTestObject('Manager Reports/Car Reports/LateEntryCar'))

WebUI.delay(5)

CustomKeywords.'com.reports.accountreports.CommonUtils.DismissUnnecessaryNotification'()

WebUI.click(findTestObject('Manager Reports/Car Reports/LateEntryCarsReport/ExcelButton'))

WebUI.click(findTestObject('Manager Reports/Car Reports/LateEntryCarsReport/PDFButton'))

WebUI.click(findTestObject('Manager Reports/Car Reports/LateEntryCarsReport/CopyButton'))

CustomKeywords.'com.managerreports.carreports.LateEntryCars.VerifyCopyButton'()

WebUI.click(findTestObject('Manager Reports/Car Reports/LateEntryCarsReport/PrintButton'))

WebUI.closeWindowIndex(1)

WebUI.switchToWindowIndex(0)

CustomKeywords.'com.reports.accountreports.CommonUtils.VerifyFileDownloaded'('Cars Late More than 2 today.xlsx')

CustomKeywords.'com.reports.accountreports.CommonUtils.VerifyFileDownloaded'('Cars Late More than 2 today.pdf')

WebUI.selectOptionByValue(findTestObject('Manager Reports/Car Reports/LateEntryCarsReport/ShowDropdown'), '50', false)

CustomKeywords.'com.managerreports.carreports.LateEntryCars.VerifyingOrder'()

CustomKeywords.'com.managerreports.carreports.LateEntryCars.verifyShowEntry'(50)

CustomKeywords.'com.managerreports.carreports.LateEntryCars.verifyPagination'()

CustomKeywords.'com.managerreports.carreports.LateEntryCars.verifyTextinRows'()

