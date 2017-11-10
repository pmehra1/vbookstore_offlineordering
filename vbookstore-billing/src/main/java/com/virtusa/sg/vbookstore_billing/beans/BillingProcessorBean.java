package com.virtusa.sg.vbookstore_billing.beans;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BillingProcessorBean {

	private static Logger log = LoggerFactory.getLogger(BillingProcessorBean.class);

	@Handler
	public void processBillingOrder(@Body String order) {
		log.info("BillingProcessorBean: processBillingOrder: order received from jms message as string = " + order);
		
		//parse incoming xml to get the orders
		//check the order details against the local data store. get the price etc for the items
		//total line items. how many are erroneous. how many are successful
		//prepare xml response
	}

}
