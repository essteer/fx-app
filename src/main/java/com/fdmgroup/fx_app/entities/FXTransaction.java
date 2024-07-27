package com.fdmgroup.fx_app.entities;

/**
 * Holds individual transaction records for further processing
 */
public class FXTransaction {
	
	private String name;
	private String fromCurrency;
	private String toCurrency;
	private double amount;
	
	
	/**
	 * 
	 * @param transactionDetails contains transaction data in String array format {"name", "fromCurrency", "toCurrency", "amount"}
	 */
	public FXTransaction(String[] transactionDetails) {
		this.name = transactionDetails[0];
		this.fromCurrency = transactionDetails[1];
		this.toCurrency = transactionDetails[2];
		this.amount = Double.valueOf(transactionDetails[3]);
	}

	/**
	 * 
	 * @return name associated with the transaction, which may or may not be a valid User name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return fromCurrency three-letter code for the currency to be converted from
	 */
	public String getFromCurrency() {
		return fromCurrency;
	}

	/**
	 * 
	 * @return toCurrency three-letter code for the currency to be converted to
	 */
	public String getToCurrency() {
		return toCurrency;
	}

	/**
	 * 
	 * @return amount the intended amount of fromCurrency to convert to toCurrency
	 */
	public double getAmount() {
		return amount;
	}
	
}
