package com.fdmgroup.fx_app.data;

import com.fdmgroup.fx_app.Currency;
import com.fdmgroup.fx_app.User;
import com.fdmgroup.fx_app.exceptions.DataSessionException;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataSession {
	
	private static DataSession dataSession;
	private static boolean initialised = false;
	private static Logger logger;
	private static Map<String,User> users;
	private static Map<String,Currency> currencies;
	
	private DataSession() {}
	
	public static void init(Map<String,User> userData, Map<String,Currency> currencyData) {
		if (initialised) {
			logger.warn("Cannot re-initialise");
		} else {
			dataSession = new DataSession();
			initialised = true;
			logger = LogManager.getLogger();
			users = userData;
			currencies = currencyData;
			logger.info("Initialisation successful");
		}
	}
	
	public static DataSession getInstance() throws DataSessionException {
		if (initialised) return dataSession;
		logger = LogManager.getLogger();
		DataSessionException exception = new DataSessionException("Not yet initialised");
		logger.error(exception);
		throw exception;
	}
	
	public static Map<String,User> getUsers() {
		Map<String,User> usersCopy = new HashMap<>();
		for (String userName : users.keySet()) {
			usersCopy.put(userName, users.get(userName));
		}
		return usersCopy;
	}
	
	public static void setUsers(Map<String,User> userData) {
		if (userData.isEmpty()) {
			logger.warn("Cannot store empty User map");
		} else if (!(userData instanceof Map<String,User>)) {
			logger.warn("Cannot update User map with invalid data type: {}", userData.getClass());
		} else {
			users = userData;
			logger.info("User list update successful");
		}
	}
	
	public static Map<String,Currency> getCurrencies() {
		return currencies;
	}

}
