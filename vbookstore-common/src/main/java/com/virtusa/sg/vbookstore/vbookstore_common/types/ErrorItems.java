package com.virtusa.sg.vbookstore.vbookstore_common.types;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ErrorItems {

	private List<String> errorItem;

	@XmlElement(name = "error-item")
	public List<String> getErrorItem() {
		return errorItem;
	}

	public void setErrorItem(List<String> errorItem) {
		this.errorItem = errorItem;
	}

}
