package com.fx.fx_app.entities;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fx.fx_app.exceptions.ConfigSettingException;
import com.fx.fx_app.utils.ConfigLoader;

public class BaseCurrency {

    private static Logger logger = LogManager.getLogger();
    private String baseCurrency;

    /**
     * Loads the base currency from config.properties.
     */
    public BaseCurrency() {
        try {
			this.baseCurrency = ConfigLoader.getProperty("base.currency");
		} catch (ConfigSettingException e) {
			logger.fatal(e.getMessage());
			System.exit(1);
		} catch (IOException | NullPointerException e) {
			logger.fatal("Config file 'src/main/resources/config.properties' not found: " + e.getMessage());
			System.exit(1);
		}
    }

    /**
     * Returns the currency code of the base currency.
     * 
     * @return baseCurrency the currency code of the base currency (e.g. usd)
     */
    public String getBaseCurrency() {
        return this.baseCurrency;
    }

}
