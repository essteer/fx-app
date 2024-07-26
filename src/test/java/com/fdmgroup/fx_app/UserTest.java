package com.fdmgroup.fx_app;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.*;

public class UserTest {
	
	@Test
	@DisplayName("User objects initialise correctly")
	public void test_User_created() {
		String name = "Demo";
		Map<String,Double> wallet = new HashMap<>();
		wallet.put("usd", 100.0);
		wallet.put("cad", 50.0);
		User user = new User(name, wallet);
		
		assertEquals(name, user.getName());
		assertEquals(wallet, user.getWallet());
	}

}
