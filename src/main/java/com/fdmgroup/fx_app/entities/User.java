package com.fdmgroup.fx_app.entities;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JSON-generated class for storing user wallet data during transaction processing
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	
	private String name;
	private Map<String,Double> wallet;
	
	/**
	 * This JSON-generated Constructor method will ignore fields in the JSON source file other than "name" and "wallet"
	 * @param name the name of the User associated with this wallet
	 * @param wallet maps currency codes to monetary values
	 */
	@JsonCreator
	public User(@JsonProperty("name") String name, @JsonProperty("wallet") Map<String,Double> wallet) {
		this.name = name;
		this.wallet = wallet;
	}
	
	/**
	 * 
	 * @return name associated with the User
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * 
	 * @return deep copy of the User's wallet
	 */
	public Map<String,Double> getWallet() {
		Map<String, Double> walletCopy = new HashMap<>();
        for (Map.Entry<String, Double> entry : wallet.entrySet()) {
            walletCopy.put(entry.getKey(), entry.getValue());
        }
        return walletCopy;
	}
	
	/**
	 * 
	 * @param currency the code of the currency to be updated in the wallet
	 * @param newAmount the new value to assign to the currency in the wallet
	 */
	public void updateWallet(String currency, double newAmount) {
		this.wallet.put(currency, newAmount);
	}
	
	/**
	 * @return String representation of the User's attributes:
	 * "name=Alice,wallet=[usd=100.0,cad=50.0,]"
	 */
	@Override
	public String toString() {
		String walletContents = "";
		for (String currency : this.wallet.keySet()) {
			walletContents += currency + ":" + wallet.get(currency) + ",";
		}
		return "name=" + this.name + ",wallet=[" + walletContents + "]";
	}
	
}
