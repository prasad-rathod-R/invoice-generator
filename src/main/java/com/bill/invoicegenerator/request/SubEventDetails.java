package com.bill.invoicegenerator.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubEventDetails {
	private Integer subEventId;
	private Integer subEventBudget;
}
