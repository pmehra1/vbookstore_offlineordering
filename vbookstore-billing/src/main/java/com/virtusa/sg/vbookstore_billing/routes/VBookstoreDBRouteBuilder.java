package com.virtusa.sg.vbookstore_billing.routes;

import org.apache.camel.builder.RouteBuilder;

import com.virtusa.sg.vbookstore.vbookstore_common.constants.Constants;
import com.virtusa.sg.vbookstore_billing.beans.DbProcessorBean;

public class VBookstoreDBRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		from("file:" + Constants.getBasePathForDataFiles() + "vbookstoredb?noop=true&fileName=bookstore_data.xml")
				.log("bookstore data reader route").bean(DbProcessorBean.class, "getBookstoreDetails");

		from("file:" + Constants.getBasePathForDataFiles() + "vbookstoredb?noop=true&fileName=customer_data.xml")
				.log("customer data reader route").bean(DbProcessorBean.class, "getCustomerDetails");

	}

}
