package com.fdmgroup.fx_app.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Currency {
	
	private String code;
	private double rate;
	private double inverseRate;
	
	@JsonCreator
	public Currency(
			@JsonProperty("code") String code, 
			@JsonProperty("rate") double rate,
			@JsonProperty("inverseRate") double inverseRate) {
		
		this.code = code;
		this.rate = rate;
		this.inverseRate = inverseRate;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public double getRate() {
		return this.rate;
	}
	
	public double getInverseRate() {
		return this.inverseRate;
	}
	
	@Override
	public String toString() {
		return this.code + " " + this.rate + " " + this.inverseRate;
	}
}
