package com.fdmgroup.fx_app;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fdmgroup.fx_app.data.DataSession;
import com.fdmgroup.fx_app.entities.Currency;

/**
 * Utility class to convert between two currencies
 * USD acts as an intermediary currency when it is not one of the two currencies directly involved
 */
public class Converter {
	
	private Logger logger = LogManager.getLogger();
	private Map<String,Currency> currencies;
	
	/**
	 * Public Constructor method initialises with Currency data from DataSession
	 */
	public Converter() {
		this.currencies = DataSession.getCurrencies();
	}
	
	/**
	 * Public entrypoint for the class, it receives a source currency and target currency, and an amount to convert from the source to the target
	 * @param fromCurrency the currency to convert from
	 * @param toCurrency the currency to convert to
	 * @param amountToConvert the amount (in fromCurrency) to convert
	 * @return double the target currency equivalent to the source currency amount
	 */
	public double convert(String fromCurrency, String toCurrency, double amountToConvert) {
		double amountInUSD = convertToUSD(fromCurrency, amountToConvert);
		double amountInTargetCurrency = convertToTargetCurrency(toCurrency, amountInUSD);
		
		return amountInTargetCurrency;
	}
	
	/**
	 * Converts a given currency into USD, ready for conversion into a target currency. If the given currency is already USD, it is returned unchanged.
	 * @param currency the currency to convert into USD
	 * @param amount the amount of the currency to convert into USD
	 * @return amount the converted amount in USD
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
	 * 
	 * Converts from USD to a target currency. If the target currency is also USD, it is returned unchanged.
	 * @param currency the currency to convert from USD into
	 * @param amount the amount of USD to convert into the target currency
	 * @return amount the amount of the target currency following conversion from USD
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
