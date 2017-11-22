package com.virtusa.sg.vbookstore_billing.routes;

import javax.xml.bind.JAXBContext;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;

import com.virtusa.sg.vbookstore.vbookstore_common.constants.Constants;
import com.virtusa.sg.vbookstore.vbookstore_common.types.InvoiceSummary;
import com.virtusa.sg.vbookstore_billing.beans.BillingEmailProcessorBean;
import com.virtusa.sg.vbookstore_billing.beans.BillingSummaryProcessorBean;

public class BillingSummaryRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		JAXBContext jaxbCtx = JAXBContext.newInstance(InvoiceSummary.class);
		JaxbDataFormat jaxbFormat = new JaxbDataFormat(jaxbCtx);

		from("jms:billingresponse").log("billing response xml ${body}").unmarshal(jaxbFormat)
				.bean(BillingSummaryProcessorBean.class).to("velocity:file:" + Constants.getBasePathForDataFiles()
						+ "velocitytemplates\\invoice_email_response.vm")
				.bean(BillingEmailProcessorBean.class);

	}

}
