package com.fx.fx_app;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fx.fx_app.data.ConfigLoader;
import com.fx.fx_app.data.DataIO;
import com.fx.fx_app.data.DataSession;
import com.fx.fx_app.data.DataValidator;
import com.fx.fx_app.entities.Currency;
import com.fx.fx_app.entities.User;
import com.fx.fx_app.exceptions.ConfigSettingException;
import com.fx.fx_app.exceptions.DataSessionException;

/**
 * @author Elliott Steer
 */
public class Main {

	/**
	 * The main entry point of the program. This method loads data from external
	 * file sources,
	 * processes transaction data, and saves the results into an output file. The
	 * program's
	 * utility classes are organized into three main sub-packages:
	 * <ul>
	 * <li>{@code data} - Classes that handle loading, storing, and validating
	 * data.</li>
	 * <li>{@code entities} - Classes that represent models such as {@code User},
	 * {@code Currency}, and {@code FXTransaction}.</li>
	 * <li>{@code exceptions} - Custom exception classes.</li>
	 * </ul>
	 * The main section of the package contains the {@code Converter} and
	 * {@code TransactionProcessor} classes,
	 * which perform high-level operations on the data and models.
	 * 
	 * @param args No optional arguments are currently supported.
	 */
	public static void main(String[] args) {

		Logger logger = LogManager.getLogger();
		DataIO loader = new DataIO();

		String usersFilePath = "";
		String currenciesFilePath = "";
		String transactionsFilePath = "";

		try {
			usersFilePath = ConfigLoader.getProperty("users.file");
			currenciesFilePath = ConfigLoader.getProperty("currencies.file");
			transactionsFilePath = ConfigLoader.getProperty("transactions.file");

		} catch (ConfigSettingException e) {
			logger.fatal(e.getMessage());
			System.exit(1);

		} catch (IOException | NullPointerException e) {
			logger.fatal("Config file 'src/main/resources/config.properties' not found: " + e.getMessage());
			System.exit(1);
		}

		File usersFile = new File(usersFilePath);
		Map<String, User> users = loader.loadUsers(usersFile);

		File currenciesFile = new File(currenciesFilePath);
		Map<String, Currency> currencies = loader.loadCurrencies(currenciesFile);

		File transactionsFile = new File(transactionsFilePath);
		List<String> transactions = loader.loadTransactions(transactionsFile);

		if (DataValidator.allDataPresent(users, currencies, transactions)) {
			try {
				DataSession.init(users, currencies);
				logger.info("Data load successful");
			} catch (DataSessionException e) {
				logger.fatal("Data load FAILURE - aborting");
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
}
