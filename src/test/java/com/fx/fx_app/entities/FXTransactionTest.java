package com.fx.fx_app.entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

/**
 * Unit tests for the FXTransaction class
 */
public class FXTransactionTest {
	
	/**
	 * Tests instance initialisation with valid attributes
	 */
	@Test
	@DisplayName("FXTransaction initialises")
	public void test_FXTransaction_initialisation() {
		assertDoesNotThrow(() ->  new FXTransaction(new String[]{"Bob", "cad", "usd", "100"}));
	}
	
	/**
	 * Tests instance has expected attributes after initialisation
	 */
	@Test
	@DisplayName("FXTransaction has expected attributes")
	public void test_FXTransaction_attributes() {
		FXTransaction fxTrade = new FXTransaction(new String[]{"Bob", "cad", "usd", "100"});
		assertEquals("Bob", fxTrade.getName());
		assertEquals("cad", fxTrade.getFromCurrency());
		assertEquals("usd", fxTrade.getToCurrency());
		assertEquals(100, fxTrade.getAmount());
	}
	
	/**
	 * Tests NumberFormatException thrown when initialised with invalid transaction amount
	 */
	@Test
	@DisplayName("FXTransaction initialises with correct attributes")
	public void test_FXTransaction_with_invalid_amount() {
		assertThrows(NumberFormatException.class, () ->  new FXTransaction(new String[]{"Bob", "cad", "usd", "xxx"}));
	}

}
