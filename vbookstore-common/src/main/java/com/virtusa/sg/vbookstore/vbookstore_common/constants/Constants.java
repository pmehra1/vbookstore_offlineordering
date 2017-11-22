package com.virtusa.sg.vbookstore.vbookstore_common.constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	private static Logger log = LoggerFactory.getLogger(Constants.class);

	public static String BOOKSTORE_DATA_XML = "";
	public static String CUSTOMER_DATA_XML = "";

	static {
		getDbFilesAsString();
	}

	public static String getBasePathForDataFiles() {
		String basePath = "";
		try {
			basePath = (new File(".")).getCanonicalPath() + "\\data\\";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return basePath;
	}

	private static void getDbFilesAsString() {
		String bookstoreFile = getBasePathForDataFiles() + "vbookstoredb\\bookstore_data.xml";
		String customerFile = getBasePathForDataFiles() + "vbookstoredb\\customer_data.xml";

		BOOKSTORE_DATA_XML = getFileAsString(bookstoreFile);
		CUSTOMER_DATA_XML = getFileAsString(customerFile);
	}

	private static String getFileAsString(String fileName) {
		StringBuilder sb = new StringBuilder();

		try {
			FileInputStream fis = new FileInputStream(new File(fileName));

			String fileAsString = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			while ((fileAsString = br.readLine()) != null) {
				sb.append(fileAsString);
			}
		} catch (FileNotFoundException e) {
			log.error("file " + fileName + " not found");
		} catch (IOException e) {
			log.error("file " + fileName + " io exception");
		}

		return sb.toString();
	}

}
