package com.fdmgroup.fx_app;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fdmgroup.fx_app.data.DataLoader;
import com.fdmgroup.fx_app.data.DataSession;
import com.fdmgroup.fx_app.entities.Currency;
import com.fdmgroup.fx_app.entities.User;
import com.fdmgroup.fx_app.exceptions.DataSessionException;

public class Main {

	public static void main(String[] args) {

		Logger logger = LogManager.getLogger();
		DataLoader loader = new DataLoader();
		
		File usersFile = new File("./src/main/resources/users.json");
		Map<String,User> users = loader.loadUsers(usersFile);
				
		File fxRatesFile = new File("./src/main/resources/fx_rates.json");
		Map<String, Currency> currencies = loader.loadCurrencies(fxRatesFile);
		
		File transactionsFile = new File("./src/main/resources/transactions.txt");
		List<String> transactions = loader.loadTransactions(transactionsFile);
		
		if (users.isEmpty() || currencies.isEmpty() || transactions.isEmpty()) {
			if (users.isEmpty()) {
				logger.error("No user data - aborting", usersFile.getName());
			}
			if (currencies.isEmpty()) {
				logger.error("No currency data - aborting", fxRatesFile.getName());
			}
			if (transactions.isEmpty()) {
				logger.error("No transaction data - aborting", transactionsFile.getName());
			}
			return;
		}
		
		try {
			DataSession.init(users, currencies);
			logger.info("Data load successful");
		} catch (DataSessionException e) {
			logger.fatal("Data load failure - aborting");
			return;
		}
		
		TransactionProcessor transactionProcessor = new TransactionProcessor();
		for (String transaction : transactions) {
			transactionProcessor.executeTransaction(transaction);
		}
        
		File newUsersFile = new File("./src/main/resources/users_updated.json");
		loader.saveUserData(newUsersFile);
		
	}
	
}
