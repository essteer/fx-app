package com.fx.fx_app;

import com.fx.fx_app.data.DataSession;
import com.fx.fx_app.data.DataValidator;
import com.fx.fx_app.entities.FXTransaction;
import com.fx.fx_app.entities.User;
import com.fx.fx_app.utils.LogHandler;

import java.util.List;
import java.util.Map;

/**
 * Validates and processes individual transactions.
 */
public class TransactionProcessor {

	private static Converter converter = new Converter();
	private String transactionsFile;
	private List<String> transactions;

	/**
	 * Initialises class with transactions source file data.
	 * 
	 * @param transactions list of strings representing individual transactions
	 */
	public TransactionProcessor(String transactionsFile, List<String> transactions) {
		this.transactionsFile = transactionsFile;
		this.transactions = transactions;
	}

	/**
	 * Iterates through each transaction and passes it to be executed.
	 */
	public void executeTransactions() {
		for (int i = 0; i < this.transactions.size(); i++) {
			String transaction = this.transactions.get(i);
			int lineInFile = i + 1;
			String sourceData = this.transactionsFile + " line=" + lineInFile; 
			executeTransaction(transaction, sourceData);
		}
	}

	/**
	 * Initiates the process of validating a transaction. If valid, it updates the associated User's data.
	 * 
	 * @param transaction a String representing an individual transaction, e.g., {@code "Bob usd hkd 100"}.
	 */
	public void executeTransaction(String transaction, String sourceData) {
		try {
			FXTransaction fxTrade = new FXTransaction(transaction.split(" "), sourceData);

			if (!(validTransaction(transaction, fxTrade))
					|| !(validUserFunds(transaction, fxTrade))) {
				return;
			}
			try {
				execute(fxTrade);
				LogHandler.transactionOK(fxTrade.toString());
			} catch (Exception e) {
				LogHandler.transactionFail(fxTrade.toString(), e.getMessage());
			}
			
		} catch (Exception e) {
			LogHandler.transactionInvalid(transaction, e.getMessage());
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
			LogHandler.transactionInvalid(fxTrade.toString());
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
			LogHandler.transactionFundsInsufficient(fxTrade.toString());
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
		LogHandler.logWalletPreTrade(fxTrade.toString(), wallet.toString());
		
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

		LogHandler.logWalletPostTrade(fxTrade.toString(), DataSession.getUser(fxTrade.getName()).getWallet().toString());
	}

}
