package com.virtusa.sg.vbookstore_billing.routes;

import javax.xml.bind.JAXBContext;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

import com.virtusa.sg.vbookstore.vbookstore_common.types.InvoiceSummary;
import com.virtusa.sg.vbookstore_billing.beans.BillingProcessorBean;

public class BillingRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		JAXBContext jaxbCtx = JAXBContext.newInstance(InvoiceSummary.class);
		JaxbDataFormat jaxbFormat = new JaxbDataFormat(jaxbCtx);

		from("jms:billingrequest").log("billing route").bean(BillingProcessorBean.class).marshal(jaxbFormat)
				.to("jms:billingresponse");

	}

}
