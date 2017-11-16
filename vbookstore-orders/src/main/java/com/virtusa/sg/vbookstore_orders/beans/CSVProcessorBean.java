package com.virtusa.sg.vbookstore_orders.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtusa.sg.vbookstore.vbookstore_common.types.Invoice;
import com.virtusa.sg.vbookstore.vbookstore_common.types.Items;
import com.virtusa.sg.vbookstore.vbookstore_common.types.LineItem;

public class CSVProcessorBean {

	private static Logger log = LoggerFactory.getLogger(CSVProcessorBean.class);

	@Handler
	public Invoice convertCSVToCannonicalXML(@Body List<Map<String, Object>> csvData) {
		log.info("CSVProcessorBean: convertCSVToCannonicalXML: csv file as map = " + csvData);

		Invoice invoice = constructInvoice(csvData);

		return invoice;
	}

	private Invoice constructInvoice(List<Map<String, Object>> csvData) {
		Invoice invoice = new Invoice();
		List<LineItem> lineItems = new ArrayList<LineItem>();

		String customerId = "";

		for (Map<String, Object> map : csvData) {
			LineItem lineItem = new LineItem();

			String id = (String) map.get("ID");
			lineItem.setId(Integer.parseInt(id));

			lineItem.setItem((String) map.get("Item"));
			lineItem.setAuthor((String) map.get("Author"));

			String quantity = (String) map.get("Quantity");
			lineItem.setQuantity(Integer.parseInt(quantity));

			String pricePerItem = (String) map.get("Price per item");
			lineItem.setItemPrice(Double.parseDouble(pricePerItem));

			lineItem.setDate((String) map.get("Date"));

			customerId = (String) map.get("Customer ID");

			lineItems.add(lineItem);
		}

		invoice.setCustomerid(customerId);
		invoice.setDateReceived((new Date()).toString());

		Items items = new Items();
		items.setItem(lineItems);
		invoice.setItems(items);

		return invoice;
	}

}
