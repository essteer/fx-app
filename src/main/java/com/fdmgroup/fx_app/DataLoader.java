package com.fdmgroup.fx_app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataLoader {

	private ObjectMapper mapper;

	public DataLoader() {
		this.mapper = new ObjectMapper();
	}
	
	public List<User> loadUsers(File file) {
		try {
			List<User> users = mapper.readValue(file, new TypeReference<List<User>>() {});
			return users;

		} catch (DatabindException | StreamReadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public Map<String,Currency> loadCurrencies(File file) {
		try {
			Map<String,Currency> currencies = mapper.readValue(file, new TypeReference<Map<String,Currency>>() {});
			return currencies;
			
		} catch (DatabindException | StreamReadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
        } catch (DatabindException | StreamReadException e) {
			e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
        	if (bufferedReader != null) {
        		try {
        		bufferedReader.close();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        	}
        }
        return lines;
	}
}
