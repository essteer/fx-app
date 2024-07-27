package com.fdmgroup.fx_app;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import com.fdmgroup.fx_app.data.Currency;

public class CurrencyTest {
	
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
		assertEquals(code + " " + rate + " " + inverseRate, usd.toString());
	}

}
