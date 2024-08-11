package com.fx.fx_app;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fx.fx_app.data.DataIO;
import com.fx.fx_app.data.DataSession;
import com.fx.fx_app.data.DataValidator;
import com.fx.fx_app.entities.Currency;
import com.fx.fx_app.entities.User;
import com.fx.fx_app.exceptions.ConfigSettingException;
import com.fx.fx_app.exceptions.DataSessionException;
import com.fx.fx_app.exceptions.DataSourceException;
import com.fx.fx_app.utils.ConfigLoader;
import com.fx.fx_app.utils.LogHandler;
import com.fx.fx_app.utils.TransactionProcessor;

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

		DataIO loader = new DataIO();
		String usersFilePath = "";
		String currenciesFilePath = "";
		String transactionsFilePath = "";
		String newUsersFilePath = "";

		try {
			usersFilePath = ConfigLoader.getProperty("users.file");
			currenciesFilePath = ConfigLoader.getProperty("currencies.file");
			transactionsFilePath = ConfigLoader.getProperty("transactions.file");
			newUsersFilePath = ConfigLoader.getProperty("newusers.file");
			LogHandler.configSettingsOK();

		} catch (ConfigSettingException e) {
			LogHandler.configSettingsError(e.getMessage());
			System.exit(1);

		} catch (IOException | NullPointerException e) {
			LogHandler.configFileLoadError(e.getMessage());
			System.exit(1);
		}

		File usersFile = new File(usersFilePath);
		Map<String, User> users = loader.loadUsers(usersFile);

		File currenciesFile = new File(currenciesFilePath);
		Map<String, Currency> currencies = loader.loadCurrencies(currenciesFile);

		File transactionsFile = new File(transactionsFilePath);
		List<String> transactions = loader.loadTransactions(transactionsFile);

		try {
			DataValidator.allDataPresent(users, currencies, transactions);
			LogHandler.sourceDataLoadOK();
		} catch (DataSourceException e) {
			LogHandler.sourceDataLoadError(e.getMessage());
			System.exit(1);
		}

		try {
			DataSession.init(users, currencies);
		} catch (DataSessionException e) {
			LogHandler.dataSessionInitError(e.getMessage());
			System.exit(1);
		}

		TransactionProcessor transactionProcessor = new TransactionProcessor(transactionsFile.getName(), transactions);
		transactionProcessor.executeTransactions();

		File newUsersFile = new File(newUsersFilePath);
		loader.saveUserData(newUsersFile);
	}
}
