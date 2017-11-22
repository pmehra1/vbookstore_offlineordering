package com.virtusa.sg.vbookstore_orders.tests;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.dataformat.CsvDataFormat;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtusa.sg.vbookstore.vbookstore_common.constants.Constants;
import com.virtusa.sg.vbookstore.vbookstore_common.types.Invoice;
import com.virtusa.sg.vbookstore_orders.beans.CSVProcessorBean;

public class CsvXmlTest extends CamelTestSupport {

	private static Logger log = LoggerFactory.getLogger(CsvXmlTest.class);

	private MockEndpoint resultEndPoint;

	@EndpointInject(uri = "direct:start")
	private ProducerTemplate template;

	@Test
	public void testXmlToCannonicalXml() throws Exception {
		String inputFile = Constants.getBasePathForDataFiles() + "csvxml\\vbookstore_order.csv";
		String outputFile = Constants.getBasePathForDataFiles() + "csvxml\\cannonical.xml";

		resultEndPoint = getMockEndpoint("mock:result");

		resultEndPoint.expectedMessageCount(1);

		template.sendBody(getFile(inputFile));

		resultEndPoint.assertIsSatisfied();

		List<Exchange> list = resultEndPoint.getReceivedExchanges();
		String body = list.get(0).getIn().getBody(String.class);
		log.debug("message body = " + body);

		String outputStr = getFileAsString(outputFile);
		outputStr = outputXMLAsStringWithDateUpdt(outputStr);
		log.debug("output string = " + outputStr);

		assertTrue(body.equals(outputStr));

	}

	public RouteBuilder createRouteBuilder() {
		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				CsvDataFormat csv = new CsvDataFormat();
				csv.setSkipHeaderRecord(true);
				csv.setUseMaps(true);

				JAXBContext jaxbCtx = JAXBContext.newInstance(Invoice.class);
				JaxbDataFormat jaxbFormat = new JaxbDataFormat(jaxbCtx);

				from("direct:start").unmarshal(csv).bean(CSVProcessorBean.class).marshal(jaxbFormat)
						.to("xslt:file:" + Constants.getBasePathForDataFiles() + "xsl\\vbookstore_order_cannonical.xsl")
						.to("mock:result");
			}
		};
	}

	private File getFile(String filePath) {
		File file = new File(filePath);
		return file;
	}

	private String getFileAsString(String file) {
		String fileAsString = context().getTypeConverter().convertTo(String.class, new File(file));
		// log.info("fileAsString = " + fileAsString);
		return fileAsString;
	}

	private String outputXMLAsStringWithDateUpdt(String outputStr) {
		outputStr = outputStr.replace("<date-received></date-received>",
				"<date-received>" + getDateAsString() + "</date-received>");
		return outputStr;
	}

	private String getDateAsString() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-ddXXX");
		return sf.format(new Date());
	}

}
