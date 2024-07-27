package com.fdmgroup.fx_app.entities;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	
	private String name;
	private Map<String,Double> wallet;
	
	@JsonCreator
	public User(@JsonProperty("name") String name, @JsonProperty("wallet") Map<String,Double> wallet) {
		this.name = name;
		this.wallet = wallet;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Map<String,Double> getWallet() {
		Map<String, Double> walletCopy = new HashMap<>();
        for (Map.Entry<String, Double> entry : wallet.entrySet()) {
            walletCopy.put(entry.getKey(), entry.getValue());
        }
        return walletCopy;
	}
	
	public void updateWallet(String currency, double newAmount) {
		this.wallet.put(currency, newAmount);
	}
	
	public String toString() {
		String walletContents = "";
		for (String currency : this.wallet.keySet()) {
			walletContents += currency + ":" + wallet.get(currency) + ",";
		}
		return "name=" + this.name + ",wallet=[" + walletContents + "]";
	}
	
}
