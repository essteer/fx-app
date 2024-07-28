package com.fdmgroup.fx_app.entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

/**
 * Unit tests for the Currency class
 */
public class CurrencyTest {
	
	/**
	 * Tests a Currency instance has expected attributes
	 */
	@Test
	@DisplayName("Currency objects initialise correctly")
	public void test_Currency_created() {
		String code = "usd";
		double rate = 1.0;
		double inverseRate = 1.0;
		Currency usd = new Currency(code, rate, inverseRate);
		
		assertEquals(code, usd.getCode());
		assertEquals(rate, usd.getRate());
		assertEquals(inverseRate, usd.getInverseRate());
	}
	
	/**
	 * Tests Currency toString method returns String in expected format
	 */
	@Test
	@DisplayName("toString method produces String in expected format")
	public void test_toString_method() {
		String code = "usd";
		double rate = 1.0;
		double inverseRate = 1.0;
		Currency usd = new Currency(code, rate, inverseRate);
		
		String expectedFormat = String.format("Currency Code: %s, Rate: %.8f, Inverse Rate: %.8f", code, rate, inverseRate);
		assertEquals(expectedFormat, usd.toString());
	}

}
