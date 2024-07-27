package com.fdmgroup.fx_app;

import com.fdmgroup.fx_app.data.DataSession;
import com.fdmgroup.fx_app.data.DataValidator;
import com.fdmgroup.fx_app.entities.FXTransaction;
import com.fdmgroup.fx_app.entities.User;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Map;

/**
 * Validates and processes individual transactions
 */
public class TransactionProcessor {

	private static Logger logger = LogManager.getLogger();
	private static Converter converter = new Converter();

	/**
	 * Public method of the class, initiates the process of validating a transaction; once validated it passes the transaction data on to update User data.
	 * @param transaction a String representing an individual transaction, e.g., "Bob usd hkd 100"
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
	 * Helper method to pass transaction data to DataValidator for validation
	 * @param transaction the original transaction to be validated
	 * @param fxTrade the transaction in FXTransaction class form
	 * @return boolean whether the transaction details are all valid
	 */
	private boolean validTransaction(String transaction, FXTransaction fxTrade) {
		if (!(DataValidator.validTransactionDetails(fxTrade))) {
			logger.error("INVALID TRANSACTION [{}] skipped", transaction);
			return false;
		}
		return true;
	}

	/**
	 * Helper method to pass transaction data to DataValidator for validation
	 * @param transaction the original transaction to be validated
	 * @param fxTrade the transaction in FXTransaction class form
	 * @return boolean whether the User associated with the transaction has sufficient funds to perform it
	 */
	private boolean validUserFunds(String transaction, FXTransaction fxTrade) {
		if (!(DataValidator.sufficientUserFunds(fxTrade))) {
			logger.error("INSUFFICIENT FUNDS [{}] skipped", transaction);
			return false;
		}
		return true;
	}

	/**
	 * Private helper method to execute a transaction once it has been validated and the associated User confirmed to hold sufficient funds
	 * @param fxTrade the transaction to be executed in FXTransaction class form
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
