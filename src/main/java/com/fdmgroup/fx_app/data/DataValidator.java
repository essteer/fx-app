package com.fdmgroup.fx_app.data;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fdmgroup.fx_app.entities.Currency;
import com.fdmgroup.fx_app.entities.FXTransaction;
import com.fdmgroup.fx_app.entities.User;
import com.fdmgroup.fx_app.exceptions.InsufficientFundsException;
import com.fdmgroup.fx_app.exceptions.InvalidCurrencyException;
import com.fdmgroup.fx_app.exceptions.UserNotFoundException;

public class DataValidator {
	
	private static Logger logger = LogManager.getLogger();
	
	/**
	 * Validates details for a single transaction by verifying that:
	 * - Target User exists
	 * - FROM and TO currencies exist
	 * - FROM and TO currencies differ
	 * - Transaction amount is positive (>0)
	 * If a check fails, subsequent checks will still complete so that all arguments can be validated for log purposes.
	 * Note that these checks are independent of a user's ability to perform the transaction - @see {sufficientUserFunds}.
	 * @param transactionDetails
	 * @return
	 * {@link UserNotFoundException}
	 */
	public static boolean validTransactionDetails(FXTransaction fxTrade) {
		boolean validDetails = true;
		if (!(validUserName(fxTrade.getName()))) {
			UserNotFoundException exception = new UserNotFoundException("User '" + fxTrade.getName() + "' not found");
			logger.error(exception);
			validDetails = false;
		}
		if (!(validCurrencyCode(fxTrade.getFromCurrency()))) {
			InvalidCurrencyException exception = new InvalidCurrencyException("FROM currency '" + fxTrade.getFromCurrency() + "' not found");
			logger.error(exception);
			validDetails = false;
		}
		if (!(validCurrencyCode(fxTrade.getToCurrency())) ) {
			InvalidCurrencyException exception = new InvalidCurrencyException("TO currency '" + fxTrade.getToCurrency() + "' not found");
			logger.error(exception);
			validDetails = false;
		}
		if (fxTrade.getFromCurrency().equals(fxTrade.getToCurrency())) {
			logger.warn("FROM and TO currencies match");
			validDetails = false;
		}
		if (!(validateTransactionValue(fxTrade.getAmount())) ) {
			logger.warn("Transaction amount '{}' invalid", fxTrade.getAmount());
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
	
	private static boolean validateTransactionValue(double amount) {
		try {
			return (amount > 0);
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
	 * {@link InsufficientFundsException}
	 */
	public static boolean sufficientUserFunds(FXTransaction fxTrade) {
		String userName = fxTrade.getName();
		User user = DataSession.getUser(userName);
		Map<String,Double> wallet = user.getWallet();
		
		String fromCurrency = fxTrade.getFromCurrency();
		if (!(wallet.containsKey(fromCurrency))) {
			String exceptionMessage = "User '" + userName + "' holds no " + fromCurrency.toUpperCase();
			InsufficientFundsException exception = new InsufficientFundsException(exceptionMessage);
			logger.error(exception);
			return false;
		}
		
		Double transactionAmount = fxTrade.getAmount();
		if (wallet.get(fromCurrency) < transactionAmount) {
			String exceptionMessage = "User '" + userName + "' " + fromCurrency.toUpperCase() + " insufficient (" + fromCurrency.toUpperCase() + wallet.get(fromCurrency) + ")";
			InsufficientFundsException exception = new InsufficientFundsException(exceptionMessage);
			logger.error(exception);
			return false;
		}
		return true;
	}
	

}
