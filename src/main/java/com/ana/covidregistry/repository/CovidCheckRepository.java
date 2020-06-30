package com.ana.covidregistry.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ana.covidregistry.model.CovidCheck;

public interface CovidCheckRepository extends MongoRepository<CovidCheck, String> {

	List<CovidCheck> findByResultIn(List<String> result);

	List<CovidCheck> findByCountryIn(List<String> country);
}