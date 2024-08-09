package com.fx.fx_app.entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class BaseCurrencyTest {

    /**
	 * Tests a BaseCurrency instance has expected attributes
	 */
	@Test
	@DisplayName("BaseCurrency initialises correctly")
	public void test_BaseCurrency_created() {
		assertDoesNotThrow(() -> new BaseCurrency());
	}

    /**
	 * Tests correct value is returned
	 */
	@Test
	@DisplayName("getBaseCurrency returns expected value")
	public void test_getBaseCurrency() {
		assertEquals("usd", new BaseCurrency().getBaseCurrency());
	}

}
