package com.fdmgroup.fx_app.entities;

public class FXTransaction {
	
	private String name;
	private String fromCurrency;
	private String toCurrency;
	private double amount;
	
	public FXTransaction(String[] transactionDetails) {
		this.name = transactionDetails[0];
		this.fromCurrency = transactionDetails[1];
		this.toCurrency = transactionDetails[2];
		this.amount = Double.valueOf(transactionDetails[3]);
	}

	public String getName() {
		return name;
	}

	public String getFromCurrency() {
		return fromCurrency;
	}

	public String getToCurrency() {
		return toCurrency;
	}

	public double getAmount() {
		return amount;
	}
	
}
