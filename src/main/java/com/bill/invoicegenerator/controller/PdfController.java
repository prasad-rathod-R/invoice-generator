package com.bill.invoicegenerator.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bill.invoicegenerator.request.UserRequest;
import com.bill.invoicegenerator.service.PdfBoxTableService;
import com.bill.invoicegenerator.service.UserPdfReportService;

@RequestMapping("/pdf")
@RestController
public class PdfController {

//	@Autowired
//	UserPdfReportService service;
	
	@Autowired
	PdfBoxTableService serv;

//	@GetMapping("/generate")
//	public ResponseEntity<Map<String, Object>> generatePdf(@RequestBody UserRequest request) {
//		return service.generatePdf(request);
//	}
	
	@GetMapping("/generate/tablewie")
	public ResponseEntity<Map<String, Object>> generatePdf2(@RequestBody UserRequest request) {
		return serv.generatePdf2(request);
	}

}
