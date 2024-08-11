package com.fx.fx_app;

import java.util.Map;

import com.fx.fx_app.data.DataSession;
import com.fx.fx_app.entities.BaseCurrency;
import com.fx.fx_app.entities.Currency;
import com.fx.fx_app.utils.LogHandler;

/**
 * Utility class to convert between two currencies.
 * USD acts as an intermediary currency when it is not one of the two currencies directly involved.
 */
public class Converter {
	
	private Map<String,Currency> currencies;
	private String baseCurrency;
	
	/**
     * Public constructor that initializes the converter with currency data from {@link DataSession}.
     */
	public Converter() {
		this.currencies = DataSession.getCurrencies();
		this.baseCurrency = new BaseCurrency().getBaseCurrency();
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
		double amountInBaseCurrency = convertToBaseCurrency(fromCurrency, amountToConvert);
		double amountInTargetCurrency = convertToTargetCurrency(toCurrency, amountInBaseCurrency);
		
		return amountInTargetCurrency;
	}
	
	/**
     * Converts an amount from the given currency to the base currency. If the given currency is already the base currency, it returns the amount unchanged.
     * 
     * @param currency the currency code to convert from
     * @param amount the amount to convert
     * @return amountInBaseCurrency the equivalent amount in the base currency
     */
	private double convertToBaseCurrency(String currency, double amount) {
		if (currency.equals(this.baseCurrency)) {
			LogHandler.noConversionNeeded();
			return amount;
		}
		Currency currency_record = this.currencies.get(currency);
		double inverseRate = currency_record.getInverseRate();
		double amountInBaseCurrency = inverseRate * amount;
		LogHandler.conversionOK(currency.toUpperCase(), amount, this.baseCurrency.toUpperCase(), amountInBaseCurrency, inverseRate);
		
		return amountInBaseCurrency;
	}
	
	/**
     * Converts an amount from the base currency to the target currency. If the target currency is also the base currency, it returns the amount unchanged.
     * 
     * @param currency the currency code to convert to
     * @param amount the amount in the base currency to convert
     * @return amountInTargetCurrency the equivalent amount in the target currency
     */
	private double convertToTargetCurrency(String currency, double amount) {
		if (currency.equals(this.baseCurrency)) {
			LogHandler.noConversionNeeded();
			return amount;
		}
		Currency currency_record = this.currencies.get(currency);
		double rate = currency_record.getRate();
		double amountInTargetCurrency = rate * amount;
		LogHandler.conversionOK(this.baseCurrency.toUpperCase(), amount, currency.toUpperCase(), amountInTargetCurrency, rate);
		
		return amountInTargetCurrency;
	}
}
