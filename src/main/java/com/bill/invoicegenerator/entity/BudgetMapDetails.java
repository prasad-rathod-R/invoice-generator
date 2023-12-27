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
@Table(name = "BUDGET_MAP_DETAILS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetMapDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BUDGET_ID")
	private Integer budgetMapId;

	@Column(name = "BUDGET_EVENT_ID")
	private Integer budgetEventId;

	@Column(name = "AMOUNT_ALLOCATED")
	private Integer amountallocvated;

	@Column(name = "SUB_EVENT_ID")
	private Integer subEventId;

	@Column(name = "SUB_EVENT_BUDGET")
	private Integer subEventBudget;

}
