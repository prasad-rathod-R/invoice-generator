package com.bill.invoicegenerator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BUDGET_DETAILS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetDetailsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BUDGET_ID")
	private Integer budgetId;

	@Column(name = "START_DATE")
	private String startDate;

	@Column(name = "BUDGET_AMOUNT")
	private String totalAmount;

}
