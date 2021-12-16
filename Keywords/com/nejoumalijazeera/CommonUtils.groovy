package com.nejoumalijazeera

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil













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
}
