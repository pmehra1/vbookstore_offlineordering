package com.virtusa.sg.vbookstore_billing.beans;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BillingProcessorBean {

	private static Logger log = LoggerFactory.getLogger(BillingProcessorBean.class);

	@Handler
	public String processBillingOrder(@Body String order) {
		log.info("BillingProcessorBean: processBillingOrder: order received from jms message as string = " + order);
		
		//parse incoming xml to get the orders
		//check the order details against the local data store. get the price etc for the items
		//total line items. how many are erroneous. how many are successful
		//prepare xml response
		
		String responseXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
				"<invoicesummary>" + 
				"	<customerid>ABC123</customerid>" + 
				"	<customeremail>customer@email.com</customeremail>" + 
				"	<date-received>2017-11-09+08:00</date-received>" + 
				"   <total-amount>250</total-amount>" +
				"	<success-items-count>2</success-items-count>" + 
				"	<error-items>" + 
				"		<error-item>Invalid quantity for Clean Code</error-item>" + 
				"		<error-item>Order quantity for Agile Dev exceeds available stock</error-item>" + 
				"	</error-items>" + 
				"</invoicesummary>";
		
		return responseXML;
	}

}
