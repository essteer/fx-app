package com.fdmgroup.fx_app;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Map;

import com.fdmgroup.fx_app.data.DataLoader;
import com.fdmgroup.fx_app.data.DataSession;
import com.fdmgroup.fx_app.exceptions.DataSessionException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) 
public class DataSessionTest {
	
	static Map<String,User> users;
	static Map<String,Currency> currencies;
	
	@BeforeAll
	static void init() {
		DataLoader loader = new DataLoader();
		File usersFile = new File("./src/test/resources/users.json");
		users = loader.loadUsers(usersFile);
		File fxRatesFile = new File("./src/test/resources/fx_rates.json");
		currencies = loader.loadCurrencies(fxRatesFile);
	}
	
	@Test
	@Order(1)
	@DisplayName("Throws DataSessionException before initialisation")
	public void test_DataSessionException_thrown_before_initialisation() {
		assertThrows(DataSessionException.class, () -> DataSession.getInstance());
	}
	
	@Test
	@Order(2)
	@DisplayName("Throws no exception if called after initialisation")
	public void test_Exception_not_thrown_after_initialisation() {
		DataSession.init(users, currencies);
		assertDoesNotThrow(() -> DataSession.getInstance());
	}
	
	@Test
	@Order(3)
	@DisplayName("Only one instance exists")
	public void test_DataSession_Singleton() {
		DataSession.init(users, currencies);
		DataSession dataSession1 = DataSession.getInstance();
		DataSession dataSession2 = DataSession.getInstance();
		assertEquals(dataSession1, dataSession2);
	}
	
	@Test
	@Order(4)
	@DisplayName("getUsers returns expected object")
	public void test_getUsers() {
		DataSession.init(users, currencies);
		assertEquals(users, DataSession.getUsers());
	}
	
	@Test
	@Order(5)
	@DisplayName("getCurrencies returns expected object")
	public void test_getCurrencies() {
		DataSession.init(users, currencies);
		assertEquals(currencies, DataSession.getCurrencies());
	}
	
	@Test
	@Order(6)
	@DisplayName("setUsers overwrites users attribute")
	public void test_setUsers() {
		DataSession.init(users, currencies);
		Map<String, User> updatedUsers = DataSession.getUsers();
		updatedUsers.remove("Bob");
		assertEquals(users, DataSession.getUsers());
		assertTrue(DataSession.getUsers().containsKey("Bob"));
		
		DataSession.setUsers(updatedUsers);
		assertEquals(updatedUsers, DataSession.getUsers());
		assertFalse(DataSession.getUsers().containsKey("Bob"));
		DataSession.setUsers(users);  // Reset Singleton instance upon test completion
	}

}
