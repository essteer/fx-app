package com.fdmgroup.fx_app;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataLoader {
	
    private static Logger logger;
	private ObjectMapper mapper;

	public DataLoader() {
		logger = LogManager.getLogger();
		this.mapper = new ObjectMapper();
	}
	
	public Map<String,User> loadUsers(File file) {
		try {
			List<User> userList = mapper.readValue(file, new TypeReference<List<User>>() {});
			logger.info("File read successful - {}", file);
			Map<String,User> userMap = new HashMap<>();
			for (User user : userList) {
				userMap.putIfAbsent(user.getName(), user);
			}
			return userMap;

		} catch (DatabindException | StreamReadException e) {
			logger.fatal(e);
		} catch (IOException e) {
			logger.fatal(e);
		}
		return Collections.emptyMap();
	}

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
}
