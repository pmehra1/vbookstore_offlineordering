package com.virtusa.sg.vbookstore_billing.routes;

import org.apache.camel.builder.RouteBuilder;

import com.virtusa.sg.vbookstore_billing.beans.BillingProcessorBean;

public class BillingRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		from("jms:billingrequest").log("billing route").bean(BillingProcessorBean.class).to("jms:billingresponse");

	}

}
