package com.virtusa.sg.vbookstore_orders.routes;

import org.apache.camel.builder.RouteBuilder;

import com.virtusa.sg.vbookstore.vbookstore_common.constants.Constants;
import com.virtusa.sg.vbookstore_orders.beans.EmailProcessorBean;

public class EmailRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		from("file:" + Constants.getBasePathForDataFiles() + "emailexcel?noop=true").log("email route")
				.bean(EmailProcessorBean.class)
				.to("xslt:file:" + Constants.getBasePathForDataFiles() + "xsl\\vbookstore_order_cannonical.xsl")
				.to("jms:billingrequest");

	}

}
