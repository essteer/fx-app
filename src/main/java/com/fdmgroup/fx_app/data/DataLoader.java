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
 * Utility class for use in loading and saving data files
 */
public class DataLoader {
	
    private static Logger logger;
	private ObjectMapper mapper;

	/**
	 * DataLoader initialises with Logger and ObjectMapper attributes
	 */
	public DataLoader() {
		logger = LogManager.getLogger();
		this.mapper = new ObjectMapper();
	}
	
	/**
	 * Loads a List of User data from the JSON @param file then passes this to {@link createUserMap} to transform into a map of names to User objects
	 * @param file to load User data from
	 * @return Map of String to User objects representing User data loaded from @param file. 
	 * Defaults to an empty Map if data cannot be loaded.
	 * {@link createUserMap}
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
	 * Receives a List of User data from {@link loadUsers} and transforms this into a map of names to User objects, which is returned to {@link loadUsers}
	 * @param userList User data received from {@link loadUsers}
	 * @return userMap Map of names to User data
	 * {@link loadUsers}
	 */
	private Map<String,User> createUserMap(List<User> userList) {
		Map<String,User> userMap = new HashMap<>();
		for (User user : userList) {
			userMap.putIfAbsent(user.getName(), user);
		}
		return userMap;
	}

	/**
	 * Loads and returns a Map of Currency data from the JSON @param file
	 * @param file to load Currency data from
	 * @return Map of String to Currency object pairs representing Currency data loaded from @param file. 
	 * Defaults to an empty Map if data cannot be loaded.
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
	 * Loads transaction data from the source .txt @param file, and passes each line as a separate String to the output file
	 * @param file to load transaction data from
	 * @return lines List of String objects representing transaction data
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
	 * Receives an File object and saves it to a JSON file with indentation
	 * @param file to be saved in JSON format
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
