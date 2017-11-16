package com.virtusa.sg.vbookstore_orders.routes;

import org.apache.camel.builder.RouteBuilder;

import com.virtusa.sg.vbookstore.vbookstore_common.constants.Constants;

public class FTPRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		from("file:" + Constants.getBasePathForDataFiles() + "ftpxml?noop=true").log("ftp route")
				.to("xslt:file:" + Constants.getBasePathForDataFiles() + "xsl\\vbookstore_order_cannonical.xsl")
				.to("jms:billingrequest");

	}

}
