package com.virtusa.sg.vbookstore_orders.constants;

import java.io.File;
import java.io.IOException;

public class Constants {

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
