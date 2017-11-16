package com.virtusa.sg.vbookstore_billing.beans;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.virtusa.sg.vbookstore.vbookstore_common.constants.Constants;
import com.virtusa.sg.vbookstore.vbookstore_common.types.Customer;
import com.virtusa.sg.vbookstore.vbookstore_common.types.ErrorItems;
import com.virtusa.sg.vbookstore.vbookstore_common.types.InvoiceSummary;
import com.virtusa.sg.vbookstore.vbookstore_common.types.LineItem;

public class BillingProcessorBean {

	private static Logger log = LoggerFactory.getLogger(BillingProcessorBean.class);

	private static XPath xpath;

	@Handler
	public InvoiceSummary processBillingOrder(@Body String order) {
		log.info("BillingProcessorBean: processBillingOrder: order received from jms message as string = " + order);

		initXPath();
		return calculateInvoiceTotal(getLineItemsFromOrder(order), order);
	}

	private static void initXPath() {
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

	private NodeList getLineItemsFromOrder(String order) {
		String expression = "/invoice:invoice/invoice:line-item";
		InputSource source = new InputSource(new StringReader(order));
		NodeList lineItems = null;

		try {
			lineItems = (NodeList) xpath.evaluate(expression, source, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			log.error("Error parsing xml. Message = " + e.getMessage());
		}

		return lineItems;

	}

	private InvoiceSummary calculateInvoiceTotal(NodeList lineItems, String order) {
		InvoiceSummary invoiceSummary = new InvoiceSummary();
		ErrorItems errorItems = new ErrorItems();
		List<String> errorItem = new ArrayList<String>();
		Integer successItemsCount = 0;
		Double totalAmount = 0.0;

		if (lineItems != null) {
			for (int i = 0; i < lineItems.getLength(); i++) {
				Node lineItemNode = lineItems.item(i);
				LineItem lineItem = getLineItemFromNode(lineItemNode);

				try {
					if (lineItemValid(lineItem)) {
						successItemsCount++;

						totalAmount += (lineItem.getQuantity() * getLineItemPrice(lineItem.getId()));
					}
				} catch (Exception e) {
					String errorMessage = e.getMessage() + " for item " + lineItem.getItem();
					errorItem.add(errorMessage);
				}
			}
		}

		errorItems.setErrorItem(errorItem);
		invoiceSummary.setErrorItem(errorItems);
		invoiceSummary.setSuccessItemsCount(successItemsCount);
		invoiceSummary.setTotalAmount(totalAmount);

		String customerId = getCustomerIdFromOrder(order);
		invoiceSummary.setCustomerid(customerId);
		invoiceSummary.setCustomeremail(getCustomerEmailById(customerId));

		invoiceSummary.setDateReceived(getDateReceivedFromOrder(order));

		log.info("invoice summary = " + invoiceSummary);

		return invoiceSummary;

	}

	private LineItem getLineItemFromNode(Node lineItemNode) {
		LineItem lineItem = new LineItem();

		NodeList items = lineItemNode.getChildNodes();
		if (items != null) {
			for (int i = 0; i < items.getLength(); i++) {
				Node item = items.item(i);
				String itemContent = item.getTextContent();
				switch (item.getNodeName()) {
				case "item-id":
					lineItem.setId(Integer.parseInt(itemContent));
					break;
				case "item-name":
					lineItem.setItem(itemContent);
					break;
				case "item-author":
					lineItem.setAuthor(itemContent);
					break;
				case "item-quantity":
					lineItem.setQuantity(Integer.parseInt(itemContent));
					break;
				case "item-price":
					lineItem.setItemPrice(Double.parseDouble(itemContent));
					break;
				case "order-date":
					lineItem.setDate(itemContent);
					break;
				}
			}
		}

		return lineItem;
	}

	private boolean lineItemValid(LineItem lineItem) throws Exception {
		boolean isValid = true;

		if (lineItem.getQuantity() < 1) {
			isValid = false;
			throw new Exception("Invalid quantity");
		}

		if (!lineItemQtyOrderedInStock(lineItem.getQuantity(), lineItem.getId())) {
			isValid = false;
			throw new Exception("Order quantity exceeds available stock");
		}

		return isValid;
	}

	private boolean lineItemQtyOrderedInStock(Integer qtyOrdered, Integer lineItemId) {
		boolean qtyInStock = true;

		log.info("qtyOrdered = " + qtyOrdered + ", line item id = " + lineItemId);
		if (qtyOrdered > getLineItemStock(lineItemId)) {
			qtyInStock = false;
		}

		return qtyInStock;
	}

	private Integer getLineItemStock(Integer lineItemId) {
		Integer lineItemStock = 0;

		LineItem lineItem = getLineItemById(lineItemId);
		log.info(
				"line item from db xml: line item id = " + lineItem.getId() + ", quantity = " + lineItem.getQuantity());
		if (lineItem != null) {
			lineItemStock = lineItem.getQuantity();
		}
		log.info("lineItemStock = " + lineItemStock);

		return lineItemStock;
	}

	private Double getLineItemPrice(Integer lineItemId) {
		Double lineItemPrice = 0.0;

		LineItem lineItem = getLineItemById(lineItemId);
		if (lineItem != null) {
			lineItemPrice = lineItem.getItemPrice();
		}

		return lineItemPrice;
	}

	private LineItem getLineItemById(Integer lineItemId) {
		String expression = "//line-item[descendant::item-id[text()=" + "'" + lineItemId + "'" + "]]";

		InputSource source = new InputSource(new StringReader(Constants.BOOKSTORE_DATA_XML));

		NodeList lineItemList = null;

		try {
			lineItemList = (NodeList) xpath.evaluate(expression, source, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			log.error("Error parsing xml. Message = " + e.getMessage());
		}

		if (lineItemList != null) {
			Node node = lineItemList.item(0);
			log.info("line item node = " + node);
			return (getLineItemFromNode(node));
		}

		return null;
	}

	private String getCustomerIdFromOrder(String order) {
		String customerId = "";

		String expression = "/invoice:invoice/invoice:customer-id";
		InputSource source = new InputSource(new StringReader(order));
		Node customerNode = null;

		try {
			customerNode = (Node) xpath.evaluate(expression, source, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			log.error("Error parsing xml. Message = " + e.getMessage());
		}

		if (customerNode != null) {
			customerId = customerNode.getTextContent();
		}

		return customerId;

	}

	private String getCustomerEmailById(String customerId) {
		String expression = "//customer[descendant::customer-id[text()=" + "'" + customerId + "'" + "]]";

		InputSource source = new InputSource(new StringReader(Constants.CUSTOMER_DATA_XML));

		NodeList customerList = null;

		try {
			customerList = (NodeList) xpath.evaluate(expression, source, XPathConstants.NODESET);
			log.info("customerList = " + customerList + " for customer id = " + customerId);
		} catch (XPathExpressionException e) {
			log.error("Error parsing xml. Message = " + e.getMessage());
		}

		if (customerList != null) {
			Node node = customerList.item(0);
			log.info("customer node = " + node);

			Customer customer = getCustomerFromNode(node);
			return customer.getCustomerEmail();
		}

		return null;
	}

	private Customer getCustomerFromNode(Node customerNode) {
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

	private String getDateReceivedFromOrder(String order) {
		String dateReceived = "";

		String expression = "/invoice:invoice/invoice:date-received";
		InputSource source = new InputSource(new StringReader(order));
		Node dateReceivedNode = null;

		try {
			dateReceivedNode = (Node) xpath.evaluate(expression, source, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			log.error("Error parsing xml. Message = " + e.getMessage());
		}

		if (dateReceivedNode != null) {
			dateReceived = dateReceivedNode.getTextContent();
		}

		return dateReceived;

	}

}
