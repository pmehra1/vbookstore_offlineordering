package com.virtusa.sg.vbookstore_billing.dao;

import java.io.StringReader;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.virtusa.sg.vbookstore.vbookstore_common.constants.Constants;
import com.virtusa.sg.vbookstore.vbookstore_common.types.Customer;

public class BillingDao {

	private static Logger log = LoggerFactory.getLogger(BillingDao.class);

	private static XPath xpath;

	public static void initXPath() {
		XPathFactory xpathFactory = XPathFactory.newInstance();

		if (xpath == null) {
			xpath = xpathFactory.newXPath();
			xpath.setNamespaceContext(new NamespaceContext() {

				@Override
				public Iterator getPrefixes(String namespaceURI) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public String getPrefix(String namespaceURI) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public String getNamespaceURI(String prefix) {
					if (prefix.equals("invoice")) {
						return "http://com.virtusa.sg/vbookstore/invoice";
					}
					return null;
				}
			});
		}
	}

	public static XPath getXPath() {
		initXPath();
		return xpath;
	}

	public static NodeList getLineItemById(Integer lineItemId) {
		String expression = "//line-item[descendant::item-id[text()=" + "'" + lineItemId + "'" + "]]";

		InputSource source = new InputSource(new StringReader(Constants.BOOKSTORE_DATA_XML));

		NodeList lineItemList = null;

		try {
			lineItemList = (NodeList) xpath.evaluate(expression, source, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			log.error("Error parsing xml " + Constants.BOOKSTORE_DATA_XML + ". Message = " + e.getMessage());
		}

		return lineItemList;
	}

	public static String getCustomerEmailById(String customerId) {
		String expression = "//customer[descendant::customer-id[text()=" + "'" + customerId + "'" + "]]";

		InputSource source = new InputSource(new StringReader(Constants.CUSTOMER_DATA_XML));

		NodeList customerList = null;

		try {
			customerList = (NodeList) xpath.evaluate(expression, source, XPathConstants.NODESET);
			log.info("customerList = " + customerList + " for customer id = " + customerId);
		} catch (XPathExpressionException e) {
			log.error("Error parsing xml " + Constants.CUSTOMER_DATA_XML + ". Message = " + e.getMessage());
		}

		if (customerList != null) {
			Node node = customerList.item(0);
			//log.info("customer node = " + node);

			Customer customer = getCustomerFromNode(node);
			return customer.getCustomerEmail();
		}

		return null;
	}

	private static Customer getCustomerFromNode(Node customerNode) {
		Customer customer = new Customer();

		NodeList customerNodes = customerNode.getChildNodes();
		if (customerNodes != null) {
			for (int i = 0; i < customerNodes.getLength(); i++) {
				Node item = customerNodes.item(i);
				String itemContent = item.getTextContent();
				switch (item.getNodeName()) {
				case "customer-id":
					customer.setCustomerId(itemContent);
					break;
				case "customer-email":
					customer.setCustomerEmail(itemContent);
					break;
				}
			}
		}

		return customer;
	}

}
