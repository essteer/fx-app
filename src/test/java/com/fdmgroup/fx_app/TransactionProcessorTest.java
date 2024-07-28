package com.fdmgroup.fx_app;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Map;

import org.junit.jupiter.api.*;

import com.fdmgroup.fx_app.data.DataIO;
import com.fdmgroup.fx_app.data.DataSession;
import com.fdmgroup.fx_app.entities.Currency;
import com.fdmgroup.fx_app.entities.User;

/**
 * Unit tests for the TransactionProcessor class
 */
public class TransactionProcessorTest {
	
	private static Map<String,User> users;
	private static Map<String, Currency> currencies;
	
	/**
	 * Initialise the DataLoader and DataSession classes with User and Currency data prior to all tests in this class
	 */
	@BeforeAll
	static void init() {
        DataIO loader = new DataIO();
		File usersFile = new File("./src/main/resources/users.json");
		users = loader.loadUsers(usersFile);
		File fxRatesFile = new File("./src/main/resources/fx_rates.json");
		currencies = loader.loadCurrencies(fxRatesFile);
		DataSession.init(users, currencies);
	}
	
	/**
	 * Tests that the executeTransaction() method runs without error when passed valid input
	 */
	@Test
	@DisplayName("Valid input throws no errors")
	public void test_valid_transaction() {
		TransactionProcessor transactionProcessor = new TransactionProcessor();
		assertDoesNotThrow(() -> transactionProcessor.executeTransaction("Bob cad usd 100"));
	}
	
	/**
	 * Tests that the executeTransaction() method does not throw errors when invalid names are passed
	 */
	@Test
	@DisplayName("Invalid name skips transaction without error")
	public void test_invalid_name() {
		TransactionProcessor transactionProcessor = new TransactionProcessor();
		assertDoesNotThrow(() -> transactionProcessor.executeTransaction("xxx cad usd 100"));
	}
	
	/**
	 * Tests that the executeTransaction() method does not throw errors when invalid FROM currencies are passed
	 */
	@Test
	@DisplayName("Invalid FROM currency skips transaction without error")
	public void test_invalid_from_currency() {
		TransactionProcessor transactionProcessor = new TransactionProcessor();
		assertDoesNotThrow(() -> transactionProcessor.executeTransaction("Bob xxx usd 100"));
	}
	
	/**
	 * Tests that the executeTransaction() method does not throw errors when invalid TO currencies are passed
	 */
	@Test
	@DisplayName("Invalid TO currency skips transaction without error")
	public void test_invalid_to_currency() {
		TransactionProcessor transactionProcessor = new TransactionProcessor();
		assertDoesNotThrow(() -> transactionProcessor.executeTransaction("Bob cad xxx 100"));
	}
	
	/**
	 * Tests that the executeTransaction() method does not throw errors when invalid transaction amounts are passed
	 */
	@Test
	@DisplayName("Invalid amount skips transaction without error")
	public void test_invalid_transaction_amount() {
		TransactionProcessor transactionProcessor = new TransactionProcessor();
		assertDoesNotThrow(() -> transactionProcessor.executeTransaction("Bob cad usd xxx"));
	}

}
