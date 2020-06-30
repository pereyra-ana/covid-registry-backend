package com.ana.covidregistry.dto;

import java.util.List;

import com.ana.covidregistry.model.CovidCheck;

public class SeachResultsDTO {
	private List<CovidCheck> results;

	public List<CovidCheck> getResults() {
		return results;
	}

	public void setResults(List<CovidCheck> list) {
		this.results = list;
	}

}
