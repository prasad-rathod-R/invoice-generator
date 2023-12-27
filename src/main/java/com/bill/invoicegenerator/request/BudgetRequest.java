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
public class BudgetRequest {

	private String sratDate;
	private Integer totalBudget;
	private List<BudgetDistribution> budgetDistribution;

}
