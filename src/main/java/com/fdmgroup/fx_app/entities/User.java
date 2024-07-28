package com.fdmgroup.fx_app.entities;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a User with a wallet containing various currencies and their amounts.
 * This class is designed to be created from JSON data.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	
	private String name;
	private Map<String,Double> wallet;
	
	/**
	 * Constructs a User object from JSON data, ignoring fields other than "name" and "wallet".
	 *
	 * @param name the name of the user
	 * @param wallet a map of currency codes to their respective amounts
	 */
	@JsonCreator
	public User(@JsonProperty("name") String name, @JsonProperty("wallet") Map<String,Double> wallet) {
		this.name = name;
		this.wallet = wallet;
	}
	
	/**
	 * Returns the name of the User.
	 *
	 * @return name the name of the User
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns a deep copy of the User's wallet.
	 *
	 * @return walletCopy a copy of the wallet containing currency codes and their amounts
	 */
	public Map<String,Double> getWallet() {
		Map<String, Double> walletCopy = new HashMap<>();
        for (Map.Entry<String, Double> entry : wallet.entrySet()) {
            walletCopy.put(entry.getKey(), entry.getValue());
        }
        return walletCopy;
	}
	
	/**
	 * Updates the amount of a specified currency in the User's wallet.
	 *
	 * @param currency the code of the currency to update
	 * @param newAmount the new amount to assign to the currency in the wallet
	 */
	public void updateWallet(String currency, double newAmount) {
		this.wallet.put(currency, newAmount);
	}
	
	/**
	 * Returns a string representation of the user's attributes.
	 *
	 * @return a string in the format "name=UserName, wallet={currencyCode:amount, ...}"
	 */
	@Override
	public String toString() {
		StringBuilder walletContents = new StringBuilder();
		for (String currency : this.wallet.keySet()) {
			walletContents.append(currency).append(":").append(wallet.get(currency)).append(", ");
		}
		// Remove the trailing comma and space if wallet is not empty
		if (walletContents.length() > 0) {
			walletContents.setLength(walletContents.length() - 2);
		}
		return "name=" + this.name + ", wallet={" + walletContents.toString() + "}";
	}
}
