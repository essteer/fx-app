package com.fx.fx_app.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class for storing currency exchange rate data.
 * This class is configured to ignore any unknown properties in the JSON source file.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Currency {
	
	private String code;
	private double rate;
	private double inverseRate;
	
	/**
	 * Constructs a Currency object with the specified code, rate, and inverseRate.
	 * This constructor is annotated to map JSON properties to the corresponding fields.
	 *
	 * @param code the three-letter code associated with the currency
	 * @param rate the amount of this currency that may be purchased with 1 USD
	 * @param inverseRate the amount of USD that may be purchased with 1 unit of this currency
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
	 * Returns the three-letter code associated with the currency.
	 *
	 * @return the currency code
	 */
	public String getCode() {
		return this.code;
	}
	
	/**
	 * Returns the amount of this currency that may be purchased with 1 USD.
	 *
	 * @return the currency rate
	 */
	public double getRate() {
		return this.rate;
	}
	
	/**
	 * Returns the amount of USD that may be purchased with 1 unit of this currency.
	 *
	 * @return the inverse currency rate
	 */
	public double getInverseRate() {
		return this.inverseRate;
	}
	
	/**
	 * Returns a string representation of the Currency's attributes in a readable format.
	 * Format: "Currency Code: [code], Rate: [rate], Inverse Rate: [inverseRate]"
	 *
	 * @return a string representation of the currency
	 */
	@Override
	public String toString() {
		return String.format("Currency Code: %s, Rate: %.8f, Inverse Rate: %.8f", this.code, this.rate, this.inverseRate);
	}
}
