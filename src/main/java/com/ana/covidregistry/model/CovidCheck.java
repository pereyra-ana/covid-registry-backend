package com.ana.covidregistry.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "covidchecks")
public class CovidCheck {
	@Id
	private String id;
	private String name;
	private String country;
	private List<String> dna;
	private String result;

	public CovidCheck() {

	}

	public CovidCheck(String name, String country, List<String> dna, String result) {
		this.name = name;
		this.country = country;
		this.dna = dna;
		this.result = result;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<String> getDna() {
		return dna;
	}

	public void setDna(List<String> dna) {
		this.dna = dna;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setId(String id) {
		this.id = id;
	}

}