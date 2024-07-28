package com.fdmgroup.fx_app.data;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fdmgroup.fx_app.entities.Currency;
import com.fdmgroup.fx_app.entities.FXTransaction;
import com.fdmgroup.fx_app.entities.User;

/**
 * Unit tests for the DataValidator class.
 */
public class DataValidatorTest {
	
	static Map<String,User> users;
	static Map<String,Currency> currencies;
	static List<String> transactions;
	
	/**
	 * Initialise the DataLoader and DataSession classes with User, Currency and transactions data prior to all tests in this class
	 */
	@BeforeAll
	static void init() {
		DataIO loader = new DataIO();
		File usersFile = new File("./src/test/resources/users.json");
		users = loader.loadUsers(usersFile);
		File fxRatesFile = new File("./src/test/resources/fx_rates.json");
		currencies = loader.loadCurrencies(fxRatesFile);
		File transactionsFile = new File("./src/main/resources/transactions.txt");
		transactions = loader.loadTransactions(transactionsFile);
		DataSession.init(users, currencies);
	}
	
	/**
	 * Tests that empty data structures will be caught and return false
	 */
	@Test
	@DisplayName("Empty data structures return false")
	public void test_missing_data() {
		assertFalse(DataValidator.allDataPresent(Collections.emptyMap(), currencies, transactions));
		assertFalse(DataValidator.allDataPresent(users, Collections.emptyMap(), transactions));
		assertFalse(DataValidator.allDataPresent(users, currencies, Collections.emptyList()));
	}
	
	/**
	 * Tests that valid data structures will return true
	 */
	@Test
	@DisplayName("Non-empty data structures return true")
	public void test_present_data() {
		assertTrue(DataValidator.allDataPresent(users, currencies, transactions));
	}
	
	/**
	 * Tests valid transaction data passes validation
	 */
	@Test
	@DisplayName("Transaction with correct number of elements return true")
	public void test_valid_transactionDetails_length() {
		FXTransaction fxTrade =  new FXTransaction(new String[]{"Bob", "cad", "usd", "100"});
		assertTrue(DataValidator.validTransactionDetails(fxTrade));
	}
	
	/**
	 * Tests invalid transaction name data fails validation
	 */
	@Test
	@DisplayName("Transaction with invalid User returns false")
	public void test_invalid_User() {
		FXTransaction fxTrade =  new FXTransaction(new String[]{"Xxx", "cad", "usd", "100"});
		assertFalse(DataValidator.validTransactionDetails(fxTrade));
	}
	
	/**
	 * Tests invalid transaction FROM currency data fails validation
	 */
	@Test
	@DisplayName("Transaction with invalid FROM currency returns false")
	public void test_invalid_FROM_currency() {
		FXTransaction fxTrade =  new FXTransaction(new String[]{"Bob", "xxx", "usd", "100"});
		assertFalse(DataValidator.validTransactionDetails(fxTrade));
	}
	
	/**
	 * Tests invalid transaction TO currency data fails validation
	 */
	@Test
	@DisplayName("Transaction with invalid TO currency returns false")
	public void test_invalid_TO_currency() {
		FXTransaction fxTrade =  new FXTransaction(new String[]{"Bob", "cad", "xxx", "100"});
		assertFalse(DataValidator.validTransactionDetails(fxTrade));
	}
	
	/**
	 * Tests matching FROM and TO currency data fails validation
	 */
	@Test
	@DisplayName("Transaction with matching FROM and TO currency returns false")
	public void test_matching_currency() {
		FXTransaction fxTrade =  new FXTransaction(new String[]{"Bob", "cad", "cad", "100"});
		assertFalse(DataValidator.validTransactionDetails(fxTrade));
	}
	
	/**
	 * Tests invalid transaction amount data fails validation
	 */
	@Test
	@DisplayName("Transaction with invalid transaction amount returns false")
	public void test_invalid_amount() {
		FXTransaction fxTrade =  new FXTransaction(new String[]{"Bob", "cad", "usd", "-100"});
		assertFalse(DataValidator.validTransactionDetails(fxTrade));
	}
	
	/**
	 * Tests FROM currency missing from User wallet fails validation
	 */
	@Test
	@DisplayName("User missing FROM currency returns false")
	public void test_User_missing_FROM_currency() {
		FXTransaction fxTrade =  new FXTransaction(new String[]{"Bob", "gbp", "usd", "100"});
		assertFalse(DataValidator.sufficientUserFunds(fxTrade));
	}
	
	/**
	 * Tests FROM currency with insufficient funds in User wallet fails validation
	 */
	@Test
	@DisplayName("User with insufficient FROM currency returns false")
	public void test_User_insufficient_FROM_currency() {
		FXTransaction fxTrade =  new FXTransaction(new String[]{"Bob", "cad", "usd", "10000000"});
		assertFalse(DataValidator.sufficientUserFunds(fxTrade));
	}
}
