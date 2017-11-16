package com.virtusa.sg.vbookstore.vbookstore_common.constants;

import java.io.File;
import java.io.IOException;

public class Constants {

	public static String BOOKSTORE_DATA_XML = "";
	public static String CUSTOMER_DATA_XML = "";

	public static String getBasePathForDataFiles() {
		String basePath = "";
		try {
			basePath = (new File(".")).getCanonicalPath() + "\\data\\";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return basePath;
	}

}
