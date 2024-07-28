package com.fdmgroup.fx_app;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fdmgroup.fx_app.data.DataSession;
import com.fdmgroup.fx_app.entities.Currency;

/**
 * Utility class to convert between two currencies.
 * USD acts as an intermediary currency when it is not one of the two currencies directly involved.
 */
public class Converter {
	
	private Logger logger = LogManager.getLogger();
	private Map<String,Currency> currencies;
	
	/**
     * Public constructor that initializes the converter with currency data from {@link DataSession}.
     */
	public Converter() {
		this.currencies = DataSession.getCurrencies();
	}
	
	/**
     * Converts an amount from one currency to another.
     * 
     * @param fromCurrency the currency code to convert from
     * @param toCurrency the currency code to convert to
     * @param amountToConvert the amount (in fromCurrency) to convert
     * @return the equivalent amount in the target currency
     */
	public double convert(String fromCurrency, String toCurrency, double amountToConvert) {
		double amountInUSD = convertToUSD(fromCurrency, amountToConvert);
		double amountInTargetCurrency = convertToTargetCurrency(toCurrency, amountInUSD);
		
		return amountInTargetCurrency;
	}
	
	/**
     * Converts an amount from the given currency to USD. If the given currency is already USD, it returns the amount unchanged.
     * 
     * @param currency the currency code to convert from
     * @param amount the amount to convert
     * @return amountInUSD the equivalent amount in USD
     */
	private double convertToUSD(String currency, double amount) {
		if (currency.equals("usd")) {
			logger.trace("Currency already usd - no conversion");
			return amount;
		}
		Currency currency_record = this.currencies.get(currency);
		double inverseRate = currency_record.getInverseRate();
		double amountInUSD = inverseRate * amount;
		logger.trace("Converted {}{} to USD{} @ rate {}", currency.toUpperCase(), amount, amountInUSD, inverseRate);
		
		return amountInUSD;
	}
	
	/**
     * Converts an amount from USD to the target currency. If the target currency is also USD, it returns the amount unchanged.
     * 
     * @param currency the currency code to convert to
     * @param amount the amount in USD to convert
     * @return amountInTargetCurrency the equivalent amount in the target currency
     */
	private double convertToTargetCurrency(String currency, double amount) {
		if (currency.equals("usd")) {
			logger.trace("Currency already usd - no conversion");
			return amount;
		}
		Currency currency_record = this.currencies.get(currency);
		double rate = currency_record.getRate();
		double amountInTargetCurrency = rate * amount;
		logger.trace("Converted USD{} to {}{} @ rate {}", amount, currency.toUpperCase(), amountInTargetCurrency, rate);
		
		return amountInTargetCurrency;
	}
}
