package com.virtusa.sg.vbookstore.vbookstore_common.types;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "invoicesummary")
public class InvoiceSummary {

	private String customerid;

	private String customeremail;

	private String dateReceived;

	private Double totalAmount;

	private Integer successItemsCount;

	private ErrorItems errorItem;

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getCustomeremail() {
		return customeremail;
	}

	public void setCustomeremail(String customeremail) {
		this.customeremail = customeremail;
	}

	@XmlElement(name = "date-received")
	public String getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(String dateReceived) {
		this.dateReceived = dateReceived;
	}

	@XmlElement(name = "total-amount")
	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	@XmlElement(name = "success-items-count")
	public Integer getSuccessItemsCount() {
		return successItemsCount;
	}

	public void setSuccessItemsCount(Integer successItemsCount) {
		this.successItemsCount = successItemsCount;
	}

	@XmlElement(name = "error-items")
	public ErrorItems getErrorItem() {
		return errorItem;
	}

	public void setErrorItem(ErrorItems errorItem) {
		this.errorItem = errorItem;
	}

}
