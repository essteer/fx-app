package com.fdmgroup.fx_app.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JSON-generated class for storing currency exchange rate data
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Currency {
	
	private String code;
	private double rate;
	private double inverseRate;
	
	/**
	 * This JSON-generated Constructor method will ignore fields in the JSON source file other than "code", "rate" and "inverseRate"
	 * @param code three-letter code associated with the currency
	 * @param rate amount of this currency that may be purchased with USD1
	 * @param inverseRate amount of USD that may be purchased with 1 unit of this currency
	 */
	@JsonCreator
	public Currency(
			@JsonProperty("code") String code, 
			@JsonProperty("rate") double rate,
			@JsonProperty("inverseRate") double inverseRate) {
		
		this.code = code;
		this.rate = rate;
		this.inverseRate = inverseRate;
	}
	
	/**
	 * 
	 * @return code three-letter code associated with the currency
	 */
	public String getCode() {
		return this.code;
	}
	
	/**
	 * 
	 * @return rate amount of this currency that may be purchased with USD1
	 */
	public double getRate() {
		return this.rate;
	}
	
	/**
	 * 
	 * @return inverseRate amount of USD that may be purchased with 1 unit of this currency
	 */
	public double getInverseRate() {
		return this.inverseRate;
	}
	
	/**
	 * @return String representation of the Currency's attributes in the format "code rate inverseRate":
	 * "gbp 0.85438980693642 1.1704259482983"
	 */
	@Override
	public String toString() {
		return this.code + " " + this.rate + " " + this.inverseRate;
	}
}
