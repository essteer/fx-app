package com.fdmgroup.fx_app;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Main {

	public static void main(String[] args) {

		DataLoader loader = new DataLoader();
		
		File usersFile = new File("./src/main/resources/users.json");
		List<User> users = loader.loadUsers(usersFile);
		
		File fxRatesFile = new File("./src/main/resources/fx_rates.json");
		Map<String, Currency> currencies = loader.loadCurrencies(fxRatesFile);
		
		File transactionsFile = new File("./src/main/resources/transactions.txt");
		List<String> transactions = loader.loadTransactions(transactionsFile);
		
		System.out.println(users.toString());
		System.out.println(currencies.toString());
		System.out.println(transactions.toString());
	}

}
