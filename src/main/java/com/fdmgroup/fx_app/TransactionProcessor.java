package com.fdmgroup.fx_app;

import com.fdmgroup.fx_app.data.DataSession;
import com.fdmgroup.fx_app.data.DataValidator;
import com.fdmgroup.fx_app.entities.FXTransaction;
import com.fdmgroup.fx_app.entities.User;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Map;


public class TransactionProcessor {

	private static Logger logger = LogManager.getLogger();
	private static Converter converter = new Converter();

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

	private boolean validTransaction(String transaction, FXTransaction fxTrade) {
		if (!(DataValidator.validTransactionDetails(fxTrade))) {
			logger.error("INVALID TRANSACTION [{}] skipped", transaction);
			return false;
		}
		return true;
	}

	private boolean validUserFunds(String transaction, FXTransaction fxTrade) {
		if (!(DataValidator.sufficientUserFunds(fxTrade))) {
			logger.error("INSUFFICIENT FUNDS [{}] skipped", transaction);
			return false;
		}
		return true;
	}

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
