package com.fx.fx_app.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Map;

import org.junit.jupiter.api.*;

import com.fx.fx_app.data.DataIO;
import com.fx.fx_app.data.DataSession;
import com.fx.fx_app.entities.Currency;
import com.fx.fx_app.entities.User;

/**
 * Unit tests for the Converter class
 */
public class ConverterTest {
	
	private static Converter converter;
	private static Map<String,User> users;
	private static Map<String, Currency> currencies;
	
	/**
	 * Initialise the DataLoader and DataSession classes with User and Currency data prior to all tests in this class, and get a Converter instance
	 */
	@BeforeAll
	static void init() {
        DataIO loader = new DataIO();
		File usersFile = new File("./src/main/resources/users.json");
		users = loader.loadUsers(usersFile);
		File fxRatesFile = new File("./src/main/resources/fx_rates.json");
		currencies = loader.loadCurrencies(fxRatesFile);
		DataSession.init(users, currencies);
		converter = new Converter();
	}
	
	/**
	 * Tests the Converter class has the convert method
	 */
	@Test
	@DisplayName("Converter has convert method")
	public void test_convert_method_present() {
		assertDoesNotThrow(() -> converter.convert("usd", "cad", 100));
	}
	
	/**
	 * Tests the original amount is returned when USD currencies match
	 */
	@Test
	@DisplayName("Converter returns original amount for USD & USD")
	public void test_convert_USD_to_USD() {
		assertEquals(100, converter.convert("usd", "usd", 100));
	}
	
	/**
	 * Tests the original amount is returned when non-USD currencies match
	 */
	@Test
	@DisplayName("Converter returns original amount for non-USD currency match")
	public void test_convert_matching_currency() {
		assertTrue(100 - converter.convert("jpy", "jpy", 100) < 0.001);
		assertTrue(100 - converter.convert("eur", "eur", 100) < 0.001);
	}
	
	/**
	 * Tests the correct USD value is obtained from a non-USD currency
	 */
	@Test
	@DisplayName("Conversion to USD correct")
	public void test_convert_to_USD_correct() {
		double amount = 100;
		double rateCADtoUSD = 0.76984464128194;
		double expected = amount * rateCADtoUSD;
		assertEquals(expected, converter.convert("cad", "usd", amount));
	}
	
	/**
	 * Tests the correct non-USD value is obtained from a USD conversion
	 */
	@Test
	@DisplayName("Conversion from USD correct")
	public void test_convert_from_USD_correct() {
		double amount = 100;
		double rateUSDtoCAD = 1.2989633834884;
		double expected = amount * rateUSDtoCAD;
		assertEquals(expected, converter.convert("usd", "cad", amount));
	}
	
	/**
	 * Tests the correct non-USD value is obtained from another non-USD conversion
	 */
	@Test
	@DisplayName("Conversion between non-USD correct")
	public void test_convert_between_non_USD() {
		double amount = 100;
		double rateCADtoUSD = 0.76984464128194;
		double rateUSDtoGBP = 0.85438980693642;
		double expected1 = amount * rateCADtoUSD * rateUSDtoGBP;
		assertEquals(expected1, converter.convert("cad", "gbp", amount));
		
		double rateGBPtoUSD = 1.1704259482983;
		double rateUSDtoCAD = 1.2989633834884;
		double expected2 = amount * rateGBPtoUSD * rateUSDtoCAD;
		assertEquals(expected2, converter.convert("gbp", "cad", amount));
	}
	
}
