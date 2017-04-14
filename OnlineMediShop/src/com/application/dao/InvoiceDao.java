package com.application.dao;

import com.application.model.Invoice;

public interface InvoiceDao {
	public Invoice generateInvoice(String orderno);
}
