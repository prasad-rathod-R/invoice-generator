package com.bill.invoicegenerator;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InvoiceGeneratorApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(InvoiceGeneratorApplication.class, args);
		System.out.println("started");
	}
}
