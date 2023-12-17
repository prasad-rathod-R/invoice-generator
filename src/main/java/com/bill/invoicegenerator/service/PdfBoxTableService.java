package com.bill.invoicegenerator.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.bill.invoicegenerator.request.UserRequest;

public interface PdfBoxTableService {
   
	public ResponseEntity<Map<String, Object>> generatePdf2(UserRequest request); 

}
