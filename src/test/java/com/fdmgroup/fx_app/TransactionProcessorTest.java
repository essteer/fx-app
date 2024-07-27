package com.fdmgroup.fx_app;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Map;

import org.junit.jupiter.api.*;

import com.fdmgroup.fx_app.data.DataLoader;
import com.fdmgroup.fx_app.data.DataSession;
import com.fdmgroup.fx_app.entities.Currency;
import com.fdmgroup.fx_app.entities.User;

public class TransactionProcessorTest {
	
	private static Map<String,User> users;
	private static Map<String, Currency> currencies;
	
	@BeforeAll
	static void init() {
        DataLoader loader = new DataLoader();
		File usersFile = new File("./src/main/resources/users.json");
		users = loader.loadUsers(usersFile);
		File fxRatesFile = new File("./src/main/resources/fx_rates.json");
		currencies = loader.loadCurrencies(fxRatesFile);
		DataSession.init(users, currencies);
	}
	
	@Test
	@DisplayName("Valid input throws no errors")
	public void test_valid_transaction() {
		TransactionProcessor transactionProcessor = new TransactionProcessor();
		assertDoesNotThrow(() -> transactionProcessor.executeTransaction("Bob cad usd 100"));
	}
	
	@Test
	@DisplayName("Invalid name skips transaction without error")
	public void test_invalid_name() {
		TransactionProcessor transactionProcessor = new TransactionProcessor();
		assertDoesNotThrow(() -> transactionProcessor.executeTransaction("xxx cad usd 100"));
	}
	
	@Test
	@DisplayName("Invalid FROM currency skips transaction without error")
	public void test_invalid_from_currency() {
		TransactionProcessor transactionProcessor = new TransactionProcessor();
		assertDoesNotThrow(() -> transactionProcessor.executeTransaction("Bob xxx usd 100"));
	}
	
	@Test
	@DisplayName("Invalid TO currency skips transaction without error")
	public void test_invalid_to_currency() {
		TransactionProcessor transactionProcessor = new TransactionProcessor();
		assertDoesNotThrow(() -> transactionProcessor.executeTransaction("Bob cad xxx 100"));
	}
	
	@Test
	@DisplayName("Invalid amount skips transaction without error")
	public void test_invalid_transaction_amount() {
		TransactionProcessor transactionProcessor = new TransactionProcessor();
		assertDoesNotThrow(() -> transactionProcessor.executeTransaction("Bob cad usd xxx"));
	}

}
