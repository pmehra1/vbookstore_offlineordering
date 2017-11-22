package com.virtusa.sg.vbookstore_billing.tests;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtusa.sg.vbookstore.vbookstore_common.constants.Constants;
import com.virtusa.sg.vbookstore.vbookstore_common.types.InvoiceSummary;
import com.virtusa.sg.vbookstore_billing.beans.BillingProcessorBean;

public class BillingTest extends CamelTestSupport {

	private static Logger log = LoggerFactory.getLogger(BillingTest.class);

	private MockEndpoint resultEndPoint;

	@EndpointInject(uri = "direct:start")
	private ProducerTemplate template;

	@Test
	public void testAllCorrectOrders() throws Exception {
		String inputFileName = "allordersvalid_request.xml";
		String outputFileName = "allordersvalid_response.xml";

		testOrders(inputFileName, outputFileName);

	}

	@Test
	public void testInvalidQuantityOrders() throws Exception {
		String inputFileName = "invalidqty_request.xml";
		String outputFileName = "invalidqty_response.xml";

		testOrders(inputFileName, outputFileName);

	}

	@Test
	public void testOutOfStockOrders() throws Exception {
		String inputFileName = "outofstock_request.xml";
		String outputFileName = "outofstock_response.xml";

		testOrders(inputFileName, outputFileName);

	}

	@Test
	public void testInvalidQtyAndOutOfStockOrders() throws Exception {
		String inputFileName = "invalidqtyoutofstock_request.xml";
		String outputFileName = "invalidqtyoutofstock_response.xml";

		testOrders(inputFileName, outputFileName);

	}

	private void testOrders(String inputFileName, String outputFileName) throws Exception {
		String inputFile = Constants.getBasePathForDataFiles() + inputFileName;
		String outputFile = Constants.getBasePathForDataFiles() + outputFileName;

		resultEndPoint = getMockEndpoint("mock:result");

		resultEndPoint.expectedMessageCount(1);

		template.sendBody(getFile(inputFile));

		resultEndPoint.assertIsSatisfied();

		List<Exchange> list = resultEndPoint.getReceivedExchanges();
		String body = list.get(0).getIn().getBody(String.class);
		body = body.replaceAll(">\\s+<", "><").trim();
		log.debug("message body = " + body);

		String outputStr = getFileAsString(outputFile);
		log.debug("output string = " + outputStr);

		assertTrue(body.equals(outputStr));
	}

	public RouteBuilder createRouteBuilder() {
		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {

				JAXBContext jaxbCtx = JAXBContext.newInstance(InvoiceSummary.class);
				JaxbDataFormat jaxbFormat = new JaxbDataFormat(jaxbCtx);

				from("direct:start").bean(BillingProcessorBean.class).marshal(jaxbFormat).to("mock:result");
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

}
