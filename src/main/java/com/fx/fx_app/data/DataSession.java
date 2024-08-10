package com.fx.fx_app.data;

import com.fx.fx_app.entities.Currency;
import com.fx.fx_app.entities.User;
import com.fx.fx_app.exceptions.DataSessionException;
import com.fx.fx_app.utils.LogHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class for managing User and Currency data throughout a session.
 */
public class DataSession {
	
	private static DataSession dataSession;
	private static boolean initialised = false;
	private static Map<String,User> users;
	private static Map<String,Currency> currencies;
	
	/**
	 * Private Constructor to prevent external use of the "new" keyword for this class.
	 */
	private DataSession() {}
	
	/**
	 * Initializes the sole instance of the DataSession class. Logs a warning for subsequent calls to this method.
	 * 
	 * @param userData Map of String to User objects representing User data
	 * @param currencyData Map of String to Currency objects representing Currency data
	 */
	public static void init(Map<String,User> userData, Map<String,Currency> currencyData) {
		if (initialised) {
			LogHandler.dataSessionInitWarn();
		} else {
			dataSession = new DataSession();
			initialised = true;
			users = userData;
			currencies = currencyData;
			LogHandler.dataSessionInitOK();
		}
	}
	
	/**
	 * Public method to obtain a reference to the sole instance of this class.
	 * 
	 * @return DataSession the sole instance of this class
	 * @throws DataSessionException on attempts to get the instance prior to initialisation
	 */
	public static DataSession getInstance() throws DataSessionException {
		if (initialised) return dataSession;
		DataSessionException exception = new DataSessionException("cannot get instance pre-init");
		throw exception;
	}
	
	/**
	 * Provides a deep copy of all User data held by this class.
	 * 
	 * @return usersCopy a deep copy of all User data held, as a Map of String names to User objects
	 */
	public static Map<String,User> getAllUsers() {
		Map<String,User> usersCopy = new HashMap<>();
		for (String userName : users.keySet()) {
			usersCopy.put(userName, users.get(userName));
		}
		return usersCopy;
	}
	
	/**
	 * Public method to return a referenced User object from the Map of all users.
	 * 
	 * @param name the name to access the requested User object from the Map
	 * @return User associated with the name
	 */
	public static User getUser(String name) {
		return users.get(name);
	}
	
	/**
	 * Public method to obtain a deep copy of all Currency data held by this class.
	 * 
	 * @return currenciesCopy a deep copy of all Currency data held, as a Map of String currency codes to Currency objects
	 */
	public static Map<String,Currency> getCurrencies() {
		Map<String,Currency> currenciesCopy = new HashMap<>();
		for (String currencyCode : currencies.keySet()) {
			currenciesCopy.put(currencyCode, currencies.get(currencyCode));
		}
		return currencies;
	}

}
