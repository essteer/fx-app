package com.fdmgroup.fx_app.data;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fdmgroup.fx_app.entities.Currency;
import com.fdmgroup.fx_app.entities.FXTransaction;
import com.fdmgroup.fx_app.entities.User;

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
	@DisplayName("Transaction with correct number of elements return true")
	public void test_valid_transactionDetails_length() {
		FXTransaction fxTrade =  new FXTransaction(new String[]{"Bob", "cad", "usd", "100"});
		assertTrue(DataValidator.validTransactionDetails(fxTrade));
	}
	
	@Test
	@DisplayName("Transaction with invalid User returns false")
	public void test_invalid_User() {
		FXTransaction fxTrade =  new FXTransaction(new String[]{"Xxx", "cad", "usd", "100"});
		assertFalse(DataValidator.validTransactionDetails(fxTrade));
	}
	
	@Test
	@DisplayName("Transaction with invalid FROM currency returns false")
	public void test_invalid_FROM_currency() {
		FXTransaction fxTrade =  new FXTransaction(new String[]{"Bob", "xxx", "usd", "100"});
		assertFalse(DataValidator.validTransactionDetails(fxTrade));
	}
	
	@Test
	@DisplayName("Transaction with invalid TO currency returns false")
	public void test_invalid_TO_currency() {
		FXTransaction fxTrade =  new FXTransaction(new String[]{"Bob", "cad", "xxx", "100"});
		assertFalse(DataValidator.validTransactionDetails(fxTrade));
	}
	
	@Test
	@DisplayName("Transaction with matching FROM and TO currency returns false")
	public void test_matching_currency() {
		FXTransaction fxTrade =  new FXTransaction(new String[]{"Bob", "cad", "cad", "100"});
		assertFalse(DataValidator.validTransactionDetails(fxTrade));
	}
	
	@Test
	@DisplayName("Transaction with invalid transaction amount returns false")
	public void test_invalid_amount() {
		FXTransaction fxTrade =  new FXTransaction(new String[]{"Bob", "cad", "usd", "-100"});
		assertFalse(DataValidator.validTransactionDetails(fxTrade));
	}
	
	@Test
	@DisplayName("User missing FROM currency returns false")
	public void test_User_missing_FROM_currency() {
		FXTransaction fxTrade =  new FXTransaction(new String[]{"Bob", "gbp", "usd", "100"});
		assertFalse(DataValidator.sufficientUserFunds(fxTrade));
	}
	
	@Test
	@DisplayName("User with insufficient FROM currency returns false")
	public void test_User_insufficient_FROM_currency() {
		FXTransaction fxTrade =  new FXTransaction(new String[]{"Bob", "cad", "usd", "10000000"});
		assertFalse(DataValidator.sufficientUserFunds(fxTrade));
	}
}
