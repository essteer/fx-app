package com.fx.fx_app.entities;

/**
 * Represents an individual foreign exchange transaction record for further processing.
 */
public class FXTransaction {
	
	private String name;
	private String fromCurrency;
	private String toCurrency;
	private double amount;
	private String sourceData;
	
	/**
	 * Constructs an FXTransaction object with the specified transaction details.
	 *
	 * @param transactionDetails an array of Strings containing transaction data in the format
	 *                           {@code {"name", "fromCurrency", "toCurrency", "amount"}}
	 * @param sourceData a String of the transactions file path plus the line the transaction appears on
	 */
	public FXTransaction(String[] transactionDetails, String sourceData) {
		this.name = transactionDetails[0];
		this.fromCurrency = transactionDetails[1];
		this.toCurrency = transactionDetails[2];
		this.amount = Double.valueOf(transactionDetails[3]);
		this.sourceData = sourceData;

	}

	/**
	 * Returns the name associated with the transaction. This name may or may not correspond to a valid User name.
	 *
	 * @return name the name associated with the transaction
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the three-letter code for the currency to be converted from.
	 *
	 * @return fromCurrency the three-letter code for the fromCurrency
	 */
	public String getFromCurrency() {
		return fromCurrency;
	}

	/**
	 * Returns the three-letter code for the currency to be converted to.
	 *
	 * @return toCurrency the three-letter code for the toCurrency
	 */
	public String getToCurrency() {
		return toCurrency;
	}

	/**
	 * Returns the intended amount of fromCurrency to be converted to toCurrency.
	 *
	 * @return amount the amount of fromCurrency to be converted
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * Returns a String represenation containing transaction data and
	 * the file path and line the data originated from.
	 * 
	 * @return a String representation of the instance
	 */
	@Override
	public String toString() {

		return this.sourceData + " {name=" + this.name + ",from=" + this.fromCurrency.toUpperCase() + ",to=" + this.toCurrency.toUpperCase() + ",amount=" + this.amount + "}";
	}
	
}
