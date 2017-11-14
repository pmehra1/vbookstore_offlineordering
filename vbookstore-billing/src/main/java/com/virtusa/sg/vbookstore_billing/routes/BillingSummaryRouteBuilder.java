package com.virtusa.sg.vbookstore_billing.routes;

import javax.xml.bind.JAXBContext;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

import com.virtusa.sg.vbookstore_billing.beans.BillingEmailProcessorBean;
import com.virtusa.sg.vbookstore_billing.beans.BillingSummaryProcessorBean;
import com.virtusa.sg.vbookstore_billing.constants.Constants;
import com.virtusa.sg.vbookstore_billing.types.InvoiceSummary;

public class BillingSummaryRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		JAXBContext jaxbCtx = JAXBContext.newInstance(InvoiceSummary.class);
		JaxbDataFormat jaxbFormat = new JaxbDataFormat(jaxbCtx);

		from("jms:billingresponse").log("billing summary route").unmarshal(jaxbFormat)
				.bean(BillingSummaryProcessorBean.class).to("velocity:file:" + Constants.getBasePathForDataFiles()
						+ "velocitytemplates\\invoice_email_response.vm")
				.bean(BillingEmailProcessorBean.class);

	}

}
