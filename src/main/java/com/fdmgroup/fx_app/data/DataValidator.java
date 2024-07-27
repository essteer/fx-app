package com.fdmgroup.fx_app.data;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataValidator {
	
	private static Logger logger = LogManager.getLogger();
	
	/**
	 * Validates details for a single transaction by verifying that:
	 * - Expected values are present
	 * - Target User exists
	 * - FROM and TO currencies exist
	 * - FROM and TO currencies differ
	 * - Transaction amount is positive (>0)
	 * As long as all expected values are present, subsequent checks will complete so that all arguments can be validated for log purposes.
	 * Note that these checks are independent of a user's ability to perform the transaction - @see {sufficientUserFunds}.
	 * @param transactionDetails
	 * @return
	 */
	public static boolean validTransactionDetails(String[] transactionDetails) {
		boolean validDetails = true;
		if (transactionDetails.length != 4) {
			logger.warn("Transaction details invalid - {}", transactionDetails.toString()); 
			return false;  // Skip further checks on corrupt data
		}
		if (!(validUserName(transactionDetails[0]))) {
			logger.warn("User '{}' not found", transactionDetails[0]);
			validDetails = false;
		}
		if (!(validCurrencyCode(transactionDetails[1]))) {
			logger.warn("FROM currency '{}' not found", transactionDetails[1]);
			validDetails = false;
		}
		if (!(validCurrencyCode(transactionDetails[2])) ) {
			logger.warn("TO currency '{}' not found", transactionDetails[2]);
			validDetails = false;
		}
		if (transactionDetails[1].equals(transactionDetails[2])) {
			logger.warn("FROM and TO currencies match");
			validDetails = false;
		}
		if (!(validateTransactionValue(transactionDetails[3])) ) {
			logger.warn("Transaction amount '{}' invalid", transactionDetails[3]);
			validDetails = false;
		}
		return validDetails;
	}
	
	private static boolean validUserName(String name) {
		Map<String,User> users = DataSession.getAllUsers();
		if (users.containsKey(name)) {
			logger.debug("User name '{}' OK", name);
			return true;
		}
		logger.debug("Invalid user  '{}'", name);
		return false;
	}
	
	
	private static boolean validCurrencyCode(String currency) {
		Map<String,Currency> currencies = DataSession.getCurrencies();
		if (currency.equals("usd") || currencies.containsKey(currency)) {
			logger.debug("Currency code '{}' OK", currency);
			return true;
		}
		logger.debug("Invalid currency  '{}'", currency);
		return false;
	}
	
	private static boolean validateTransactionValue(String amount) {
		try {
			return (Double.valueOf(amount) > 0);
		} catch (NumberFormatException e) {
			return false;  // Logged by parent method
		}
	}
	
	/**
	 * Validates that a User is able to support a transaction, for a transaction
	 * for which the details have already been validated @see {validTransactionDetails}. Checks that:
	 * - User holds the currency to be converted FROM
	 * - User holds enough of the FROM currency to support the transaction amount
	 * @param transactionDetails
	 * @return
	 */
	public static boolean sufficientUserFunds(String[] transactionDetails) {
		String userName = transactionDetails[0];
		User user = DataSession.getUser(userName);
		Map<String,Double> wallet = user.getWallet();
		
		String fromCurrency = transactionDetails[1];
		if (!(wallet.containsKey(fromCurrency))) {
			logger.warn("User '{}' holds no {} - cannot convert FROM {}", userName, fromCurrency, fromCurrency);
			return false;
		}
		
		Double transactionAmount = Double.valueOf(transactionDetails[3]);
		if (wallet.get(fromCurrency) < transactionAmount) {
			logger.warn("User '{}' holds {}{} - cannot convert FROM {}{}", userName, fromCurrency, wallet.get(fromCurrency), fromCurrency, transactionAmount);
			return false;
		}
		return true;
	}
	

}
