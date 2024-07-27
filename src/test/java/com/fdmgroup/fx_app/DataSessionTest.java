package com.fdmgroup.fx_app;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.fdmgroup.fx_app.data.Currency;
import com.fdmgroup.fx_app.data.DataLoader;
import com.fdmgroup.fx_app.data.DataSession;
import com.fdmgroup.fx_app.data.User;
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
	@DisplayName("getAllUsers returns expected object")
	public void test_getAllUsers() {
		DataSession.init(users, currencies);
		assertEquals(users, DataSession.getAllUsers());
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
	@DisplayName("updateUserWallet overwrites existing wallet")
	public void test_updateUserWallet() {
		DataSession.init(users, currencies);
		User user = DataSession.getUser("Bob");
		
		Map<String,Double> newWallet = new HashMap<>();
		newWallet.put("xxx", 99.99);
		
		assertNotEquals(newWallet, user.getWallet());
		
		user.updateWallet(newWallet);
		assertEquals(newWallet, DataSession.getUser("Bob").getWallet());
	}

}
