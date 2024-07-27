package com.fdmgroup.fx_app;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fdmgroup.fx_app.data.Currency;
import com.fdmgroup.fx_app.data.DataLoader;
import com.fdmgroup.fx_app.data.DataSession;
import com.fdmgroup.fx_app.data.DataValidator;
import com.fdmgroup.fx_app.data.User;

public class DataValidatorTest {
	
	static Map<String,User> users;
	static Map<String,Currency> currencies;
	static List<String> transactions;
	
	@BeforeAll
	static void init() {
		DataLoader loader = new DataLoader();
		File usersFile = new File("./src/test/resources/users.json");
		users = loader.loadUsers(usersFile);
		File fxRatesFile = new File("./src/test/resources/fx_rates.json");
		currencies = loader.loadCurrencies(fxRatesFile);
		File transactionsFile = new File("./src/main/resources/transactions.txt");
		transactions = loader.loadTransactions(transactionsFile);
		DataSession.init(users, currencies);
	}
	
	@Test
	@DisplayName("Transactions with wrong number of elements return false")
	public void test_invalid_transactionDetails_length() {
		String[] tooShort = {"Bob", "cad", "usd"};
		assertFalse(DataValidator.validTransactionDetails(tooShort));
		String[] tooLong = {"Bob", "cad", "usd", "100", "etc"};
		assertFalse(DataValidator.validTransactionDetails(tooLong));
	}
	
	@Test
	@DisplayName("Transaction with correct number of elements return false")
	public void test_valid_transactionDetails_length() {
		String[] correctLength = {"Bob", "cad", "usd", "100"};
		assertTrue(DataValidator.validTransactionDetails(correctLength));
	}
	
	@Test
	@DisplayName("Transaction with invalid User returns false")
	public void test_invalid_User() {
		String[] testcase = {"Xxx", "cad", "usd", "100"};
		assertFalse(DataValidator.validTransactionDetails(testcase));
	}
	
	@Test
	@DisplayName("Transaction with invalid FROM currency returns false")
	public void test_invalid_FROM_currency() {
		String[] testcase = {"Bob", "xxx", "usd", "100"};
		assertFalse(DataValidator.validTransactionDetails(testcase));
	}
	
	@Test
	@DisplayName("Transaction with invalid TO currency returns false")
	public void test_invalid_TO_currency() {
		String[] testcase = {"Bob", "cad", "xxx", "100"};
		assertFalse(DataValidator.validTransactionDetails(testcase));
	}
	
	@Test
	@DisplayName("Transaction with matching FROM and TO currency returns false")
	public void test_matching_currency() {
		String[] testcase = {"Bob", "cad", "cad", "100"};
		assertFalse(DataValidator.validTransactionDetails(testcase));
	}
	
	@Test
	@DisplayName("Transaction with invalid transaction amount returns false")
	public void test_invalid_amount() {
		String[] testcase1 = {"Bob", "cad", "usd", "-100"};
		assertFalse(DataValidator.validTransactionDetails(testcase1));
		String[] testcase2 = {"Bob", "cad", "usd", "Bob"};
		assertFalse(DataValidator.validTransactionDetails(testcase2));
	}
	
	@Test
	@DisplayName("User missing FROM currency returns false")
	public void test_User_missing_FROM_currency() {
		String[] testcase = {"Bob", "gbp", "usd", "100"};
		assertFalse(DataValidator.sufficientUserFunds(testcase));
	}
	
	@Test
	@DisplayName("User with insufficient FROM currency returns false")
	public void test_User_insufficient_FROM_currency() {
		String[] testcase = {"Bob", "cad", "usd", "10000000"};
		assertFalse(DataValidator.sufficientUserFunds(testcase));
	}

}
