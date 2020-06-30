package com.ana.covidregistry.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ana.covidregistry.dto.SeachResultsDTO;
import com.ana.covidregistry.model.CovidCheck;
import com.ana.covidregistry.model.Stats;
import com.ana.covidregistry.service.CovidCheckService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CovidCheckController {

	@Autowired
	private CovidCheckService covidCheckService;

	@GetMapping("/covid/checks")
	public ResponseEntity<SeachResultsDTO> getAll() {
		try {
			SeachResultsDTO covidChecks = this.covidCheckService.getAll();

			if (covidChecks.getResults().isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(covidChecks, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/covid/checks/search")
	public ResponseEntity<SeachResultsDTO> getAllWithFilter(@RequestParam(required = false) String key,
			@RequestParam(required = false) String value) {
		try {
			SeachResultsDTO covidChecks = this.covidCheckService.getAllWithFilter(key, value);

			if (covidChecks.getResults().isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(covidChecks, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/covid/stats")
	public ResponseEntity<Stats> getStats() {
		try {
			Stats stats = this.covidCheckService.getStats();

			return new ResponseEntity<>(stats, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/covid/checks/{id}")
	public ResponseEntity<CovidCheck> getById(@PathVariable("id") String id) {
		Optional<CovidCheck> covidCheckData = this.covidCheckService.getById(id);

		if (covidCheckData.isPresent()) {
			return new ResponseEntity<>(covidCheckData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/covid/checks")
	public ResponseEntity<CovidCheck> create(@RequestBody CovidCheck covidCheck) {
		try {
			CovidCheck _covidCheck = this.covidCheckService.create(covidCheck);
			return new ResponseEntity<>(_covidCheck, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}
}