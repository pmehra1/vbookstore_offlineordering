package com.virtusa.sg.vbookstore_orders.routes;

import javax.xml.bind.JAXBContext;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.dataformat.CsvDataFormat;

import com.virtusa.sg.vbookstore_orders.beans.CSVProcessorBean;
import com.virtusa.sg.vbookstore_orders.constants.Constants;
import com.virtusa.sg.vbookstore_orders.types.Invoice;

public class CSVRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		CsvDataFormat csv = new CsvDataFormat();
		csv.setSkipHeaderRecord(true);
		csv.setUseMaps(true);

		JAXBContext jaxbCtx = JAXBContext.newInstance(Invoice.class);
		JaxbDataFormat jaxbFormat = new JaxbDataFormat(jaxbCtx);

		from("file:" + Constants.getBasePathForDataFiles() + "httpcsv?noop=true").log("csv route").unmarshal(csv)
				.bean(CSVProcessorBean.class).marshal(jaxbFormat)
				.to("xslt:file:" + Constants.getBasePathForDataFiles() + "xsl\\vbookstore_order_cannonical.xsl")
				.to("jms:billingrequest");

	}

}
