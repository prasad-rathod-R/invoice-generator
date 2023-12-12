package com.bill.invoicegenerator.service;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface InvoiceService {

    Map<String, Object> getCustomerInfo();

    List<Map<String, Object>> getInvoiceItems();

    ResponseEntity<Map<String, Object>> generateInvoicePDF();
}
