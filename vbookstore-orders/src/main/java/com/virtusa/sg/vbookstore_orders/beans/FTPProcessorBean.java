package com.virtusa.sg.vbookstore_orders.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FTPProcessorBean {

	private static Logger log = LoggerFactory.getLogger(FTPProcessorBean.class);

	@Handler
	public InputStream convertXMLToCannonicalXML(@Body InputStream in) {
		log.info("FTPProcessorBean: convertXMLToCannonicalXML: ftp xml file as input stream = " + in);
		displayFileContent(in);
		return in;
	}

	private void displayFileContent(InputStream in) {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		try {
			log.info("FTPProcessorBean: displayFileContent: content of xml file");
			String str = "";
			while ((str = br.readLine()) != null) {
				log.info(str);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
