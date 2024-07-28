package com.fdmgroup.fx_app.data;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Map;

import com.fdmgroup.fx_app.entities.Currency;
import com.fdmgroup.fx_app.entities.User;
import com.fdmgroup.fx_app.exceptions.DataSessionException;

/**
 * Unit tests for the DataSession class.
 * Note that some tests may fail if run in conjunction with the rest of the test suite.
 * This is a consequence of checks against this Singleton pattern that may be interfered with
 * by other tests run simultaneously. If needed, run this test file on its own to ensure tests pass.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) 
public class DataSessionTest {
	
	static Map<String,User> users;
	static Map<String,Currency> currencies;
	
	/**
	 * Initialise the class with a DataLoader and User and Currency data prior to all tests in this class
	 */
	@BeforeAll
	static void init() {
		DataIO loader = new DataIO();
		File usersFile = new File("./src/test/resources/users.json");
		users = loader.loadUsers(usersFile);
		File fxRatesFile = new File("./src/test/resources/fx_rates.json");
		currencies = loader.loadCurrencies(fxRatesFile);
	}
	
	/**
	 * Tests a DataSessionException is thrown if getInstance() is called prior to initialisation
	 */
	@Test
	@Order(1)
	@DisplayName("Throws DataSessionException before initialisation")
	public void test_DataSessionException_thrown_before_initialisation() {
		assertThrows(DataSessionException.class, () -> DataSession.getInstance());
	}
	
	/**
	 * Tests no exception is thrown if getInstance() is called after initialisation
	 */
	@Test
	@Order(2)
	@DisplayName("Throws no exception if called after initialisation")
	public void test_Exception_not_thrown_after_initialisation() {
		DataSession.init(users, currencies);
		assertDoesNotThrow(() -> DataSession.getInstance());
	}
	
	/**
	 * Tests only one instance exists when called multiple times
	 */
	@Test
	@Order(3)
	@DisplayName("Only one instance exists")
	public void test_DataSession_Singleton() {
		DataSession.init(users, currencies);
		DataSession dataSession1 = DataSession.getInstance();
		DataSession dataSession2 = DataSession.getInstance();
		assertEquals(dataSession1, dataSession2);
	}
	
	/**
	 * Tests User data can be accessed correctly
	 */
	@Test
	@Order(4)
	@DisplayName("getAllUsers returns expected object")
	public void test_getAllUsers() {
		DataSession.init(users, currencies);
		assertEquals(users, DataSession.getAllUsers());
	}
	
	/**
	 * Tests Currency data can be accessed correctly
	 */
	@Test
	@Order(5)
	@DisplayName("getCurrencies returns expected object")
	public void test_getCurrencies() {
		DataSession.init(users, currencies);
		assertEquals(currencies, DataSession.getCurrencies());
	}
	
}
