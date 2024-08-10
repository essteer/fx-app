package com.fx.fx_app.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fx.fx_app.entities.BaseCurrency;
import com.fx.fx_app.entities.Currency;
import com.fx.fx_app.entities.FXTransaction;
import com.fx.fx_app.entities.User;
import com.fx.fx_app.exceptions.DataSourceException;
import com.fx.fx_app.exceptions.InsufficientFundsException;
import com.fx.fx_app.exceptions.InvalidCurrencyException;
import com.fx.fx_app.exceptions.UserNotFoundException;
import com.fx.fx_app.utils.LogHandler;

/**
 * Utility class for validating data received from external sources.
 */
public class DataValidator {
	
	private static Logger logger = LogManager.getLogger();
	private static String baseCurrency = new BaseCurrency().getBaseCurrency();

	/**
	 * Checks whether essential data is present before proceeding with data processing.
	 * 
	 * @param users Map of String names to User objects
	 * @param currencies Map of String currency codes to Currency objects
	 * @param transactions List of String objects representing transactions
	 * @return boolean {@code false} if any of the arguments are empty, otherwise {@code true}
	 */
	public static boolean allDataPresent(Map<String,User> users, Map<String,Currency> currencies, List<String> transactions) throws DataSourceException {
		
		if (currencies.isEmpty() || transactions.isEmpty() || users.isEmpty()) {
			
			List<String> missingData = new ArrayList<>();
			
			if (currencies.isEmpty()) {
				missingData.add("CURRENCY");
			}
			if (transactions.isEmpty()) {
				missingData.add("TRANSACTION");
			}
			if (users.isEmpty()) {
				missingData.add("USER");
			}
			throw new DataSourceException(missingData.toString());
		}
		return true;
	}
	
	/**
	 * Validates details for a single transaction by verifying that:
	 * <ul>
	 *   <li>Target User exists</li>
	 *   <li>FROM and TO currencies exist</li>
	 *   <li>FROM and TO currencies differ</li>
	 *   <li>Transaction amount is positive (&gt;0)</li>
	 * </ul>
	 * If a single check fails, subsequent checks will still complete so that all arguments 
	 * can be validated for logging purposes. Note that these checks are independent of a 
	 * User's ability to perform the transaction. See {@link #sufficientUserFunds(FXTransaction)}.
	 * 
	 * @param fxTrade the FXTransaction object containing the relevant transaction data
	 * @return boolean {@code true} for valid transactions, otherwise {@code false}
	 * @see InvalidCurrencyException
	 * @see UserNotFoundException
	 */
	public static boolean validTransactionDetails(FXTransaction fxTrade) {
		boolean validDetails = true;
		if (!(validUserName(fxTrade.getName()))) {
			UserNotFoundException exception = new UserNotFoundException(fxTrade.getName());
			LogHandler.userNotFound(fxTrade.toString(), exception.getMessage());
			validDetails = false;
		}
		if (!(validCurrencyCode(fxTrade.getFromCurrency()))) {
			InvalidCurrencyException exception = new InvalidCurrencyException(fxTrade.getFromCurrency());
			LogHandler.invalidCurrency("FROM", fxTrade.toString(), exception.getMessage());
			validDetails = false;
		}
		if (!(validCurrencyCode(fxTrade.getToCurrency())) ) {
			InvalidCurrencyException exception = new InvalidCurrencyException(fxTrade.getToCurrency());
			LogHandler.invalidCurrency("TO", fxTrade.toString(), exception.getMessage());
			validDetails = false;
		}
		if (fxTrade.getFromCurrency().equals(fxTrade.getToCurrency())) {
			LogHandler.currenciesMatch(fxTrade.toString());
			validDetails = false;
		}
		if (!(validateTransactionValue(fxTrade.getAmount())) ) {
			LogHandler.invalidAmount(fxTrade.toString());
			validDetails = false;
		}
		return validDetails;
	}
	
	/**
	 * Checks whether a name is contained in the DataSession Map of User objects.
	 * 
	 * @param name the name to access the User object by
	 * @return boolean {@code true} for valid User names, otherwise {@code false}
	 */
	private static boolean validUserName(String name) {
		Map<String,User> users = DataSession.getAllUsers();
		if (users.containsKey(name)) {
			LogHandler.userNamePresentInSession(name);
			return true;
		}
		LogHandler.userNameNotPresentInSession(name);
		return false;
	}
	
	/**
	 * Checks whether a currency code is associated with a Currency object contained in 
	 * the DataSession Map of Currency objects.
	 * 
	 * @param currency the three-letter code to access the Currency object by
	 * @return boolean {@code true} for valid Currency codes, otherwise {@code false}
	 */
	private static boolean validCurrencyCode(String currency) {

		Map<String,Currency> currencies = DataSession.getCurrencies();
		
		if (currency.equals(baseCurrency) || currencies.containsKey(currency)) {
			LogHandler.currencyPresentInSession(currency);
			return true;
		}
		LogHandler.currencyNotPresentInSession(currency);
		return false;
	}
	
	/**
	 * Validates the transaction amount to ensure it is positive.
	 * 
	 * @param amount the transaction amount to be validated
	 * @return boolean {@code true} for valid transaction amounts, otherwise {@code false}
	 */
	private static boolean validateTransactionValue(double amount) {
		try {
			return (amount > 0);
		} catch (NumberFormatException e) {
			return false;  // Logged by parent method
		}
	}
	
	/**
	 * Validates that a User is able to support a transaction, for a transaction
	 * for which the details have already been validated. See {@link #validTransactionDetails(FXTransaction)}. 
	 * Checks that:
	 * <ul>
	 *   <li>User holds the currency to be converted FROM</li>
	 *   <li>User holds enough of the FROM currency to support the transaction amount</li>
	 * </ul>
	 * 
	 * @param fxTrade the FXTransaction object containing the relevant transaction data
	 * @return {@code true} if the User has sufficient funds, otherwise {@code false}
	 * @see InsufficientFundsException
	 */
	public static boolean sufficientUserFunds(FXTransaction fxTrade) {
		String userName = fxTrade.getName();
		User user = DataSession.getUser(userName);
		Map<String,Double> wallet = user.getWallet();
		
		String fromCurrency = fxTrade.getFromCurrency();
		if (!(wallet.containsKey(fromCurrency))) {
			String exceptionMessage = "User holds no " + fromCurrency.toUpperCase();
			InsufficientFundsException exception = new InsufficientFundsException(exceptionMessage);
			LogHandler.userFundsInsufficient(wallet.toString(), fxTrade.toString(), exception.getMessage());
			return false;
		}
		
		Double transactionAmount = fxTrade.getAmount();
		if (wallet.get(fromCurrency) < transactionAmount) {
			String exceptionMessage = "User " + fromCurrency.toUpperCase() + " insufficient";
			InsufficientFundsException exception = new InsufficientFundsException(exceptionMessage);
			logger.error(exception);
			LogHandler.userFundsInsufficient(wallet.toString(), fxTrade.toString(), exception.getMessage());
			return false;
		}
		return true;
	}

}
