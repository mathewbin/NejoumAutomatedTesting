<?xml version="1.0" encoding="UTF-8"?>
<TestSuiteEntity>
   <description></description>
   <name>LoginPage</name>
   <tag></tag>
   <isRerun>false</isRerun>
   <mailRecipient></mailRecipient>
   <numberOfRerun>0</numberOfRerun>
   <pageLoadTimeout>30</pageLoadTimeout>
   <pageLoadTimeoutDefault>true</pageLoadTimeoutDefault>
   <rerunFailedTestCasesOnly>false</rerunFailedTestCasesOnly>
   <rerunImmediately>false</rerunImmediately>
   <testSuiteGuid>b295f874-fd4f-410e-8f27-7e8c946224cf</testSuiteGuid>
   <testCaseLink>
      <guid>67261699-ca29-42ee-affa-ed13a09fdb77</guid>
      <isReuseDriver>false</isReuseDriver>
      <isRun>true</isRun>
      <testCaseId>Test Cases/LoginPage/TC_VerifyLoginToApplication</testCaseId>
      <testDataLink>
         <combinationType>ONE</combinationType>
         <id>6a15df5a-5d7e-4b65-9765-c131861b1747</id>
         <iterationEntity>
            <iterationType>ALL</iterationType>
            <value></value>
         </iterationEntity>
         <testDataId>Data Files/ValidUsers</testDataId>
      </testDataLink>
      <variableLink>
         <testDataLinkId>6a15df5a-5d7e-4b65-9765-c131861b1747</testDataLinkId>
         <type>DATA_COLUMN</type>
         <value>email</value>
         <variableId>6f4cd524-f142-4273-b206-b46aae650aac</variableId>
      </variableLink>
      <variableLink>
         <testDataLinkId>6a15df5a-5d7e-4b65-9765-c131861b1747</testDataLinkId>
         <type>DATA_COLUMN</type>
         <value>password</value>
         <variableId>dc594dff-d4ee-446a-b857-a17f24b343ec</variableId>
      </variableLink>
   </testCaseLink>
   <testCaseLink>
      <guid>4ed659b6-1fba-4aae-8789-4c1cf9f1b53b</guid>
      <isReuseDriver>false</isReuseDriver>
      <isRun>true</isRun>
      <testCaseId>Test Cases/LoginPage/TC_VerifyLoginWithInvalidPwd</testCaseId>
      <testDataLink>
         <combinationType>ONE</combinationType>
         <id>53429146-a56a-4e40-b23c-0897b1e8bc6e</id>
         <iterationEntity>
            <iterationType>ALL</iterationType>
            <value></value>
         </iterationEntity>
         <testDataId>Data Files/InvalidUsers</testDataId>
      </testDataLink>
      <variableLink>
         <testDataLinkId>53429146-a56a-4e40-b23c-0897b1e8bc6e</testDataLinkId>
         <type>DATA_COLUMN</type>
         <value>email</value>
         <variableId>c9b92dc6-a25c-43c8-8adb-8f1215b2b9bd</variableId>
      </variableLink>
      <variableLink>
         <testDataLinkId>53429146-a56a-4e40-b23c-0897b1e8bc6e</testDataLinkId>
         <type>DATA_COLUMN</type>
         <value>password</value>
         <variableId>79c0b2b5-90fe-44f9-ac85-6302ca7eade8</variableId>
      </variableLink>
   </testCaseLink>
   <testCaseLink>
      <guid>80c513a6-684b-4b26-9e0d-c5b98e0a469d</guid>
      <isReuseDriver>false</isReuseDriver>
      <isRun>true</isRun>
      <testCaseId>Test Cases/LoginPage/TC_VerifyLoginWithoutData</testCaseId>
   </testCaseLink>
</TestSuiteEntity>
