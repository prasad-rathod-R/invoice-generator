package com.bill.invoicegenerator.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BudgetDistribution {

	private Integer event_id;
	private Integer allocated_budget;
	private List<SubEventDetails> subEventDetails;
}
