package com.virtusa.sg.vbookstore_billing.beans;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.component.velocity.VelocityConstants;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtusa.sg.vbookstore.vbookstore_common.types.ErrorItems;
import com.virtusa.sg.vbookstore.vbookstore_common.types.InvoiceSummary;

public class BillingSummaryProcessorBean {

	private static Logger log = LoggerFactory.getLogger(BillingSummaryProcessorBean.class);

	@Handler
	public InvoiceSummary processBillingOrderSummary(@Body InvoiceSummary invoiceSummary, Exchange exchange) {
		log.info("BillingSummaryProcessorBean: processBillingOrderSummary: invoice summary received as object = "
				+ invoiceSummary);

		log.info("customer id = " + invoiceSummary.getCustomerid() + ", customer email = "
				+ invoiceSummary.getCustomeremail() + ", order date received = " + invoiceSummary.getDateReceived()
				+ ", total amount = " + invoiceSummary.getTotalAmount() + ", successfully processed items count = "
				+ invoiceSummary.getSuccessItemsCount());

		ErrorItems errorItems = invoiceSummary.getErrorItem();

		if (errorItems.getErrorItem() != null) {
			log.info("Items not processed:");
			for (String error : errorItems.getErrorItem()) {
				log.info(error);
			}
		}
		
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("invoiceSummary", invoiceSummary);
		
		VelocityContext velocityCtx = new VelocityContext(variableMap);
		exchange.getIn().setHeader(VelocityConstants.VELOCITY_CONTEXT, velocityCtx);

		return invoiceSummary;
	}

}
