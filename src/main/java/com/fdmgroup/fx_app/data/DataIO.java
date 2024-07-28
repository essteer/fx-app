package com.fdmgroup.fx_app.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fdmgroup.fx_app.entities.Currency;
import com.fdmgroup.fx_app.entities.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for loading and saving data files.
 */
public class DataIO {
	
    private static Logger logger;
	private ObjectMapper mapper;

	/**
	 * Initialises with Logger and ObjectMapper attributes.
	 */
	public DataIO() {
		logger = LogManager.getLogger();
		this.mapper = new ObjectMapper();
	}
	
	/**
	 * Loads a list of User data from the specified JSON file, transforms it into a Map of String names to User objects, and returns the Map.
	 *
	 * @param file the file to load User data from
	 * @return userMap a map of String to User objects representing the User data loaded from the file. 
	 *         Defaults to an empty map if data cannot be loaded.
	 */
	public Map<String,User> loadUsers(File file) {
		try {
			List<User> userList = mapper.readValue(file, new TypeReference<List<User>>() {});
			logger.info("File read successful - {}", file);
			Map<String,User> userMap = createUserMap(userList);
			logger.debug("User map creation successful");
			return userMap;

		} catch (DatabindException | StreamReadException e) {
			logger.fatal(e);
		} catch (IOException e) {
			logger.fatal(e);
		}
		return Collections.emptyMap();
	}
	
	/**
	 * Transforms a List of User data into a map of String names to User objects.
	 *
	 * @param userList the list of User data to be transformed
	 * @return userMap a Map of String names to User objects
	 */
	private Map<String,User> createUserMap(List<User> userList) {
		Map<String,User> userMap = new HashMap<>();
		for (User user : userList) {
			userMap.putIfAbsent(user.getName(), user);
		}
		return userMap;
	}

	/**
	 * Loads and returns a Map of Currency data from the specified JSON file.
	 *
	 * @param file the file to load Currency data from
	 * @return currencyMap a Map of String to Currency objects representing the Currency data loaded from the file. 
	 *         Defaults to an empty Map if data cannot be loaded.
	 */
	public Map<String,Currency> loadCurrencies(File file) {
		try {
			Map<String,Currency> currencies = mapper.readValue(file, new TypeReference<Map<String,Currency>>() {});
			logger.info("File read successful - {}", file);
			return currencies;
			
		} catch (DatabindException | StreamReadException e) {
			logger.fatal(e);
		} catch (IOException e) {
			logger.fatal(e);
		}
		return Collections.emptyMap();
	}
	
	/**
	 * Loads transaction data from the specified text file and returns each line as a separate String in a List.
	 *
	 * @param file the file to load transaction data from
	 * @return lines a List of String objects representing the transaction data
	 */
	public List<String> loadTransactions(File file) {
		List<String> lines = new ArrayList<>();
		BufferedReader bufferedReader = null;
		
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			String line = bufferedReader.readLine();
			while (line != null) {
				lines.add(line);
				line = bufferedReader.readLine();
			}
			logger.info("File read successful - {}", file);
			
        } catch (DatabindException | StreamReadException e) {
        	logger.fatal(e);
        } catch (IOException e) {
        	logger.fatal(e);

        } finally {
        	if (bufferedReader != null) {
        		try {
        		bufferedReader.close();
        		} catch (IOException e) {
        			logger.warn("BufferedReader failed to close");
        		}
        	}
        }
        return lines;
	}
	
	/**
	 * Saves User data to a JSON file with indentation.
	 *
	 * @param file the file to save updated User data to
	 */
	public void saveUserData(File file) {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writeValue(file, DataSession.getAllUsers());
            logger.info("User data save SUCCESS - {}", file.getName());
        
        } catch (IOException e) {
            logger.error("User data save FAIL - {}", e);
        }
	}
}
