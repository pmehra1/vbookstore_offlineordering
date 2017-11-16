package com.virtusa.sg.vbookstore_orders.beans;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.virtusa.sg.vbookstore.vbookstore_common.types.Invoice;
import com.virtusa.sg.vbookstore.vbookstore_common.types.Items;
import com.virtusa.sg.vbookstore.vbookstore_common.types.LineItem;

public class EmailProcessorBean {

	private static Logger log = LoggerFactory.getLogger(EmailProcessorBean.class);

	@Handler
	public Invoice convertExcelToCannonicalXML(@Body InputStream in) {
		log.info("EmailProcessorBean: convertExcelToCannonicalXML: excel file as input stream = " + in);

		Invoice invoice = constructInvoice(in);

		return invoice;
	}

	private Invoice constructInvoice(InputStream in) {
		Invoice invoice = new Invoice();
		List<LineItem> lineItems = new ArrayList<LineItem>();

		String customerId = "";

		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(in);

			Sheet sheet = wb.getSheetAt(0);

			int count = 0;

			for (Row row : sheet) {
				if (count == 0) {
					count++;
					continue;
				}

				LineItem lineItem = new LineItem();

				Cell idCell = row.getCell(0);
				Double dId = Double.parseDouble(getCellContentByType(idCell));
				lineItem.setId(dId.intValue());

				Cell itemCell = row.getCell(1);
				lineItem.setItem(getCellContentByType(itemCell));

				Cell authorCell = row.getCell(2);
				lineItem.setAuthor(getCellContentByType(authorCell));

				Cell qtyCell = row.getCell(3);
				Double dQty = Double.parseDouble(getCellContentByType(qtyCell));
				lineItem.setQuantity(dQty.intValue());

				Cell itemPriceCell = row.getCell(4);
				lineItem.setItemPrice(Double.parseDouble(getCellContentByType(itemPriceCell)));

				Cell dateCell = row.getCell(5);
				lineItem.setDate(getCellContentByType(dateCell));

				Cell customeridCell = row.getCell(6);
				customerId = getCellContentByType(customeridCell);

				lineItems.add(lineItem);

				count++;
			}

		} catch (EncryptedDocumentException e) {
			log.error("Encrypted document exception. Message = " + e.getMessage());
		} catch (InvalidFormatException e) {
			log.error("Invalid format exception. Message = " + e.getMessage());
		} catch (IOException e) {
			log.error("IO exception. Message = " + e.getMessage());
		}

		invoice.setCustomerid(customerId);
		invoice.setDateReceived((new Date()).toString());

		Items items = new Items();
		items.setItem(lineItems);
		invoice.setItems(items);

		return invoice;
	}

	private String getCellContentByType(Cell cell) {
		String cellValue = "";

		switch (cell.getCellTypeEnum()) {
		case STRING:
			cellValue = cell.getRichStringCellValue().getString();
			break;
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				cellValue = cell.getDateCellValue().toString();
			} else {
				cellValue = String.valueOf(cell.getNumericCellValue());
			}
			break;
		default:
			log.info("cell type not string or numeric");
		}

		return cellValue;
	}

}
