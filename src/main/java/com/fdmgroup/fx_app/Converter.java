package com.fdmgroup.fx_app;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fdmgroup.fx_app.data.Currency;
import com.fdmgroup.fx_app.data.DataSession;

public class Converter {
	
	private Logger logger = LogManager.getLogger();
	private Map<String,Currency> currencies;
	
	public Converter() {
		this.currencies = DataSession.getCurrencies();
	}
	
	public double convert(String fromCurrency, String toCurrency, double amountToConvert) {
		double amountInUSD = convertToUSD(fromCurrency, amountToConvert);
		double amountInTargetCurrency = convertToTargetCurrency(toCurrency, amountInUSD);
		
		return amountInTargetCurrency;
	}
	
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
