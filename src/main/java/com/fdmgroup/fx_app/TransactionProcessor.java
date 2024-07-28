package com.fdmgroup.fx_app;

import com.fdmgroup.fx_app.data.DataSession;
import com.fdmgroup.fx_app.data.DataValidator;
import com.fdmgroup.fx_app.entities.FXTransaction;
import com.fdmgroup.fx_app.entities.User;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Map;

/**
 * Validates and processes individual transactions.
 */
public class TransactionProcessor {

	private static Logger logger = LogManager.getLogger();
	private static Converter converter = new Converter();

	/**
	 * Initiates the process of validating a transaction. If valid, it updates the associated User's data.
	 * 
	 * @param transaction a String representing an individual transaction, e.g., {@code "Bob usd hkd 100"}.
	 */
	public void executeTransaction(String transaction) {
		try {
			FXTransaction fxTrade = new FXTransaction(transaction.split(" "));

			if (!(validTransaction(transaction, fxTrade))
					|| !(validUserFunds(transaction, fxTrade))) {
				return;
			}
			try {
				execute(fxTrade);
				logger.info("Transaction SUCCESS [{}]", transaction);
			} catch (Exception e) {
				logger.error("VALID TRANSACTION FAILED [{}]: {}", transaction, e);
			}
			
		} catch (Exception e) {
			logger.error("INVALID TRANSACTION [{}] skipped", transaction);
		}
	}

	/**
	 * Validates the transaction details using {@link DataValidator}.
	 * 
	 * @param transaction the original transaction to be validated
	 * @param fxTrade the transaction in {@link FXTransaction} form
	 * @return boolean {@code true} if all transaction details are all valid, otherwise {@code false}
	 */
	private boolean validTransaction(String transaction, FXTransaction fxTrade) {
		if (!(DataValidator.validTransactionDetails(fxTrade))) {
			logger.error("INVALID TRANSACTION [{}] skipped", transaction);
			return false;
		}
		return true;
	}

	/**
	 * Validates if the User associated with the transaction has sufficient funds.
	 * 
	 * @param transaction the original transaction to be validated
	 * @param fxTrade the transaction in {@link FXTransaction} form
	 * @return boolean {@code true} if the User has sufficient funds to perform the transaction, otherwise {@code false}
	 */
	private boolean validUserFunds(String transaction, FXTransaction fxTrade) {
		if (!(DataValidator.sufficientUserFunds(fxTrade))) {
			logger.error("INSUFFICIENT FUNDS [{}] skipped", transaction);
			return false;
		}
		return true;
	}

	/**
	 * Executes a transaction once it has been validated and the associated User confirmed to hold sufficient funds.
	 * 
	 * @param fxTrade the transaction to be executed in {@link FXTransaction} form
	 */
	private void execute(FXTransaction fxTrade) {

		User user = DataSession.getUser(fxTrade.getName());
		Map<String, Double> wallet = user.getWallet();
		logger.info("'{}' wallet pre-update: {}", fxTrade.getName(), wallet.toString());
		
		String fromCurrency = fxTrade.getFromCurrency();
		String toCurrency = fxTrade.getToCurrency();
		Double amount = fxTrade.getAmount();
		Double convertedAmount = converter.convert(fromCurrency, toCurrency, amount);

		double fromCurrencyValue = wallet.get(fromCurrency);
		double newFromCurrencyValue = (Math.round((fromCurrencyValue - amount) * 100.0) / 100.0);
		user.updateWallet(fromCurrency, newFromCurrencyValue);

		double toCurrencyValue = wallet.getOrDefault(toCurrency, 0.0);
		double newToCurrencyValue = (Math.round((toCurrencyValue + convertedAmount) * 100.0) / 100.0);
		user.updateWallet(toCurrency, newToCurrencyValue);

		logger.info("'{}' wallet post-update: {}", fxTrade.getName(), DataSession.getUser(fxTrade.getName()).getWallet());

	}

}
