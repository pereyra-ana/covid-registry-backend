package com.ana.covidregistry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ana.covidregistry.controller.CovidCheckController;
import com.ana.covidregistry.dto.SeachResultsDTO;
import com.ana.covidregistry.model.CovidCheck;
import com.ana.covidregistry.model.Stats;
import com.ana.covidregistry.service.CovidCheckService;

@SpringBootTest
class CovidRegistryApplicationTests {

	@Autowired
	private CovidCheckController controller;

	@Autowired
	private CovidCheckService service;

	private ResponseEntity<CovidCheck> createCheck(String name, String country, String dnaString) {
		CovidCheck covidCheckCreate = new CovidCheck();
		covidCheckCreate.setName(name);
		covidCheckCreate.setCountry(country);

		List<String> dna = new ArrayList<>();
		for (String string : dnaString.split(" ")) {
			dna.add(string);
		}
		covidCheckCreate.setDna(dna);

		return this.controller.create(covidCheckCreate);
	}

	@Test()
	void createWithErrors() {
		// sin los argumentos requeridos, falla la invocacion directo
		CovidCheck cc = new CovidCheck();
		ResponseEntity<CovidCheck> response = this.controller.create(cc);
		assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());

		// con adn con letras fuera de las permitidas
		String randomString = String.valueOf(Math.random());
		response = this.createCheck("Test" + randomString, "Argentina", "ATFCGA CGGTGC TTATGT AGAAGG CCCCTA TCACTG");
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		// con cantidad de elementos por cadena distinto a cantidad de cadenas (distinto
		// a NxN)
		response = this.createCheck("Test" + randomString, "Argentina", "AT CGTGC TTATGT AGAAGG CCCCTA TCACTG");
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	ResponseEntity<CovidCheck> createInfected() {
		String randomString = String.valueOf(Math.random());

		ResponseEntity<CovidCheck> response = this.createCheck("Test" + randomString, "Argentina",
				"ATGCGA CGGTGC TTATGT AGAAGG CCCCTA TCACTG");

		assertNotNull(response.getBody());
		assertEquals("Test" + randomString, response.getBody().getName());
		assertEquals("Argentina", response.getBody().getCountry());
		assertEquals("Infectado", response.getBody().getResult());
		assertNotNull(response.getBody().getId());

		return response;
	}

	@Test
	ResponseEntity<CovidCheck> createHealthy() {
		String randomString = String.valueOf(Math.random());

		ResponseEntity<CovidCheck> response = this.createCheck("Test" + randomString, "Argentina",
				"ATGCGA CGGTAC TTATGT AGAAGG CCCCTA TCACTG");

		assertNotNull(response.getBody());
		assertEquals("Test" + randomString, response.getBody().getName());
		assertEquals("Argentina", response.getBody().getCountry());
		assertEquals("Sano", response.getBody().getResult());
		assertNotNull(response.getBody().getId());

		return response;
	}

	@Test
	ResponseEntity<CovidCheck> createImmune() {
		String randomString = String.valueOf(Math.random());

		ResponseEntity<CovidCheck> response = this.createCheck("Test" + randomString, "Argentina",
				"ATTTTA CCCCGC TTTTGT AAAAGG CCCCTA TCACTG");

		assertNotNull(response.getBody());
		assertEquals("Test" + randomString, response.getBody().getName());
		assertEquals("Argentina", response.getBody().getCountry());
		assertEquals("Inmune", response.getBody().getResult());
		assertNotNull(response.getBody().getId());

		return response;
	}

	@Test
	void getById() {
		String randomString = String.valueOf(Math.random());
		ResponseEntity<CovidCheck> response = this.createCheck("Test" + randomString, "Argentina",
				"ATTTTA CCCCGC TTTTGT AAAAGG CCCCTA TCACTG");
		ResponseEntity<CovidCheck> responseDetail = this.controller.getById(response.getBody().getId());

		assertNotNull(responseDetail.getBody());

		this.service.deleteById(response.getBody().getId());
	}

	@Test
	void getByIdNotFound() {
		String id = "test";
		ResponseEntity<CovidCheck> response = this.controller.getById(id);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void getAll() {
		ResponseEntity<CovidCheck> responseCreate = this.createHealthy();
		ResponseEntity<SeachResultsDTO> response = this.controller.getAll();
		assertTrue(response.getBody().getResults().size() > 0, "Error");

		this.service.deleteById(responseCreate.getBody().getId());
	}

	@Test
	void getAllWithFilter() {
		String randomString = String.valueOf(Math.random());
		ResponseEntity<CovidCheck> responseCreate = this.createCheck("Test" + randomString, "Argentina",
				"ATGCGA CGGTAC TTATGT AGAAGG CCCCTA TCACTG"); // sano

		ResponseEntity<SeachResultsDTO> response = this.controller.getAllWithFilter("country", "Argentina");
		assertTrue(response.getBody().getResults().size() >= 1);

		ResponseEntity<SeachResultsDTO> response2 = this.controller.getAllWithFilter("country", "Argentina,Brasil");
		assertTrue(response2.getBody().getResults().size() >= 1);

		ResponseEntity<SeachResultsDTO> response3 = this.controller.getAllWithFilter("result", "Sano");
		assertTrue(response3.getBody().getResults().size() >= 1);

		ResponseEntity<SeachResultsDTO> response4 = this.controller.getAllWithFilter("result", "Sano,Infectado");
		assertTrue(response4.getBody().getResults().size() >= 1);

		this.service.deleteById(responseCreate.getBody().getId());
	}

	@Test
	void getAllWithFilterWithError() {
		ResponseEntity<SeachResultsDTO> response = this.controller.getAllWithFilter("testKey", "testValue");
		// si la key no la conozco, lanzo una excepcion
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void getStats() {
		ResponseEntity<Stats> responseBefore = this.controller.getStats();
		Long healthyBefore = responseBefore.getBody().getHealthy();
		Long infectedBefore = responseBefore.getBody().getInfected();
		Long immuneBefore = responseBefore.getBody().getImmune();

		ResponseEntity<CovidCheck> responseHealthy = this.createHealthy();
		ResponseEntity<CovidCheck> responseInfected = this.createInfected();
		ResponseEntity<CovidCheck> responseImmune = this.createImmune();

		ResponseEntity<Stats> responseNow = this.controller.getStats();
		assertEquals(healthyBefore + 1, responseNow.getBody().getHealthy());
		assertEquals(immuneBefore + 1, responseNow.getBody().getImmune());
		assertEquals(infectedBefore + 1, responseNow.getBody().getInfected());

		this.service.deleteById(responseHealthy.getBody().getId());
		this.service.deleteById(responseInfected.getBody().getId());
		this.service.deleteById(responseImmune.getBody().getId());

	}

}
