package com.fdmgroup.fx_app.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.*;

/**
 * Unit tests for the User class
 */
public class UserTest {
	
	private String name;
	private Map<String,Double> wallet;
	private User user;
	
	/**
	 * Initialise common attributes for test cases
	 */
	@BeforeEach
	void init() {
		this.name = "Demo";
		this.wallet = new HashMap<>();
		wallet.put("usd", 100.0);
		wallet.put("cad", 50.0);
		this.user = new User(name, wallet);
	}
	
	/**
	 * Tests a User instance has expected attributes
	 */
	@Test
	@DisplayName("User objects initialise correctly")
	public void test_User_created() {
		assertEquals(name, user.getName());
		assertEquals(wallet, user.getWallet());
	}
	
	/**
	 * Tests the updateWallet() method successfully updates wallet contents
	 */
	@Test
	@DisplayName("updateWallet overwrites existing wallet")
	public void test_updateWallet() {
		Map<String,Double> newWallet = new HashMap<>();
		newWallet.put("usd", 100.0);
		newWallet.put("cad", 50.0);
		newWallet.put("xxx", 99.99);
		
		assertNotEquals(newWallet, user.getWallet());
		
		user.updateWallet("xxx", 99.99);
		assertEquals(newWallet, user.getWallet());
	}

}
