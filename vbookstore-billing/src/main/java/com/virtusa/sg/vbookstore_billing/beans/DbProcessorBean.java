package com.virtusa.sg.vbookstore_billing.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.camel.Body;

import com.virtusa.sg.vbookstore.vbookstore_common.constants.Constants;

public class DbProcessorBean {

	public void getBookstoreDetails(@Body InputStream in) {
		Constants.BOOKSTORE_DATA_XML = getStringXML(in);
	}

	public void getCustomerDetails(@Body InputStream in) {
		Constants.CUSTOMER_DATA_XML = getStringXML(in);
	}

	private String getStringXML(InputStream in) {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		try {
			String str = "";
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

}
