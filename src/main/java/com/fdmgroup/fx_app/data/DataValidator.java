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

/**
 * Utility class for use in validating data received from external sources
 */
public class DataValidator {
	
	private static Logger logger = LogManager.getLogger();
	
	/**
	 * Validates details for a single transaction by verifying that:
	 * <ol>
	 * <li>Target User exists</li>
	 * <li>FROM and TO currencies exist</li>
	 * <li>FROM and TO currencies differ</li>
	 * <li>Transaction amount is positive (>0)</li>
	 * </ol>
	 * If a check fails, subsequent checks will still complete so that all arguments can be validated for log purposes.
	 * Note that these checks are independent of a user's ability to perform the transaction - @see {sufficientUserFunds}.
	 * @param fxTrade the FXTransaction object containing the relevant transaction data
	 * @return boolean true for valid transactions otherwise false <br>
	 * {@link InvalidCurrencyException} <br>
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
	
	/**
	 * Checks whether a name is contained in the DataSession Map of User objects
	 * @param name the name to access the User object by
	 * @return boolean whether the name relates to a valid User objects
	 */
	private static boolean validUserName(String name) {
		Map<String,User> users = DataSession.getAllUsers();
		if (users.containsKey(name)) {
			logger.debug("User name '{}' OK", name);
			return true;
		}
		logger.debug("Invalid user  '{}'", name);
		return false;
	}
	
	/**
	 * Checks whether a currency code is associated with a Currency object contained in the DataSession Map of Currency objects
	 * @param currency the three-letter code to access the Currency object by
	 * @return boolean whether the currency code relates to a valid Currency object
	 */
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
	 * <ol>
	 * <li>User holds the currency to be converted FROM</li>
	 * <li>User holds enough of the FROM currency to support the transaction amount</li>
	 * </ol>
	 * @param fxTrade the FXTransaction object containing the relevant transaction data
	 * @return boolean whether the User associated with the transaction has sufficient funds <br>
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
