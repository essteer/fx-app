package com.fdmgroup.fx_app.data;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Collections;

/**
 * Unit tests for the DataLoader class
 */
public class DataIOTest {
	
	static DataIO dataLoader;
	
	/**
	 * Initialise the class prior to all tests in this class
	 */
	@BeforeAll
	static void init() {
		dataLoader = new DataIO();
	}
	
	/**
	 * Tests an empty Map is returned for invalid User data
	 */
	@Test
	@DisplayName("Invalid Users file returns empty Map")
	public void test_loadUsers_invalid_file() {
		assertEquals(Collections.emptyMap(), dataLoader.loadUsers(new File("ABC")));
	}
	
	/**
	 * Tests valid User data is loaded correctly
	 */
	@Test
	@DisplayName("Valid Users file returns expected content")
	public void test_loadUsers_valid_file() {
		assertEquals(3, dataLoader.loadUsers(new File("./src/test/resources/users.json")).size());
	}
	
	/**
	 * Tests an empty Map is returned for invalid Currency data
	 */
	@Test
	@DisplayName("Invalid FX file returns empty Map")
	public void test_loadCurrencies_invalid_file() {
		assertEquals(Collections.emptyMap(), dataLoader.loadCurrencies(new File("ABC")));
	}
	
	/**
	 * Tests valid Currency data is loaded correctly
	 */
	@Test
	@DisplayName("Valid FX file returns expected content")
	public void test_loadCurrencies_valid_file() {
		assertEquals(148, dataLoader.loadCurrencies(new File("./src/main/resources/fx_rates.json")).size());
	}
	
	/**
	 * Tests an empty List is returned for invalid transaction data
	 */
	@Test
	@DisplayName("Invalid transactions file returns empty List")
	public void test_loadTransactions_invalid_file() {
		assertEquals(Collections.emptyList(), dataLoader.loadTransactions(new File("ABC")));
	}
	
	/**
	 * Tests valid transaction data is loaded correctly
	 */
	@Test
	@DisplayName("Valid transactions file returns expected content")
	public void test_loadTransactions_valid_file() {
		assertEquals(5, dataLoader.loadTransactions(new File("./src/test/resources/transactions.txt")).size());
	}

}
