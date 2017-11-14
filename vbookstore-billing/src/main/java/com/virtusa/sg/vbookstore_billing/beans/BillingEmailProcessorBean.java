package com.virtusa.sg.vbookstore_billing.beans;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BillingEmailProcessorBean {

	private static Logger log = LoggerFactory.getLogger(BillingEmailProcessorBean.class);

	@Handler
	public String processBillingEmail(@Body String emailBody) {
		log.info("BillingEmailProcessorBean: processBillingEmail: email body received as string = " + emailBody);

		return "";
	}

}
