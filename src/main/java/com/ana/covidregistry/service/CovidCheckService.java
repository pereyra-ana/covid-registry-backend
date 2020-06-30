package com.ana.covidregistry.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ana.covidregistry.dto.SeachResultsDTO;
import com.ana.covidregistry.enums.ChainLetterEnum;
import com.ana.covidregistry.enums.ResultEnum;
import com.ana.covidregistry.model.CovidCheck;
import com.ana.covidregistry.model.Stats;
import com.ana.covidregistry.repository.CovidCheckRepository;

@Service
public class CovidCheckService {

	@Autowired
	private CovidCheckRepository covidCheckRepository;

	private static final String[] DNA_CHAIN_LETTERS = { ChainLetterEnum.A.getValue(), ChainLetterEnum.C.getValue(),
			ChainLetterEnum.G.getValue(), ChainLetterEnum.T.getValue() };

	private static final int CHAR_REPEAT_TIMES = 4;

	private static final String ALLOWEDCHARREGEX = "^[acgtACGT/ ]*$";

	private Integer countRepetitions(final List<String> dnaChains) {
		Integer repetitions = 0;
		for (String dnaChain : dnaChains) {
			for (String letter : DNA_CHAIN_LETTERS) {
				String chain = StringUtils.repeat(letter, CHAR_REPEAT_TIMES);

				if (dnaChain.contains(chain)) {
					repetitions += 1;
				}
			}
		}
		return repetitions;
	}

	private String getResult(Integer repetitions) {
		String result;
		switch (repetitions) {
		case 0:
		case 1:
			result = ResultEnum.HEALTY.getResult();
			break;
		case 2:
		case 3:
			result = ResultEnum.INFECTED.getResult();
			break;
		default:
			result = ResultEnum.IMMUNE.getResult();
			break;
		}
		return result;
	}

	private String getDnaResult(final List<String> dna) {
		List<String> verticalDnaChains = new ArrayList<>();
		int repetitions = 0;

		for (String dnaChain : dna) {
			for (int j = 0; j < dnaChain.length(); j++) {
				// armo nuevas cadenas con los elementos i de cada cadena - cadenas verticales
				if (verticalDnaChains.size() > j) {
					verticalDnaChains.set(j, verticalDnaChains.get(j).concat(dnaChain.substring(j, j + 1)));
				} else {
					verticalDnaChains.add(dnaChain.substring(j, j + 1));
				}
			}
		}

		repetitions += this.countRepetitions(dna); // horizontal
		repetitions += this.countRepetitions(verticalDnaChains); // vertical

		return getResult(repetitions);
	}

	public CovidCheck create(final CovidCheck covidCheck) {
		if (!validateInput(covidCheck)) {
			throw new IllegalArgumentException();
		}
		String dnaResult = this.getDnaResult(covidCheck.getDna());

		return covidCheckRepository
				.save(new CovidCheck(covidCheck.getName(), covidCheck.getCountry(), covidCheck.getDna(), dnaResult));
	}

	private boolean validateInput(final CovidCheck cc) {
		Boolean invalidDna = false;
		Boolean isNxN = true;
		Integer countChains = cc.getDna().size();

		for (String chain : cc.getDna()) {
			Integer countChainLetters = chain.length();
			if (!countChainLetters.equals(countChains)) {
				isNxN = false;
				break;
			}

			if (!chain.matches(ALLOWEDCHARREGEX)) {
				invalidDna = true;
				break;
			}
		}
		if (Boolean.TRUE.equals(invalidDna) || Boolean.FALSE.equals(isNxN))
			return false;

		return true;
	}

	public SeachResultsDTO getAll() {
		SeachResultsDTO covidChecks = new SeachResultsDTO();
		List<CovidCheck> results = covidCheckRepository.findAll();
		covidChecks.setResults(results);

		return covidChecks;

	}

	public SeachResultsDTO getAllWithFilter(final String key, final String value) {
		SeachResultsDTO covidChecks = new SeachResultsDTO();
		List<CovidCheck> results;

		List<String> values = new ArrayList<>();
		for (String string : value.split(",")) {
			values.add(string);
		}

		if ("country".equals(key)) {
			results = covidCheckRepository.findByCountryIn(values);
		} else if ("result".equals(key)) {
			results = covidCheckRepository.findByResultIn(values);
		} else {
			throw new IllegalArgumentException();
		}

		covidChecks.setResults(results);

		return covidChecks;
	}

	public Stats getStats() {
		List<CovidCheck> results = covidCheckRepository.findAll();

		long hr = results.stream().filter(r -> r.getResult().equals(ResultEnum.HEALTY.getResult())).count();
		long inr = results.stream().filter(r -> r.getResult().equals(ResultEnum.INFECTED.getResult())).count();
		long imr = results.stream().filter(r -> r.getResult().equals(ResultEnum.IMMUNE.getResult())).count();

		Stats stats = new Stats();
		stats.setHealthy(hr);
		stats.setInfected(inr);
		stats.setImmune(imr);

		return stats;
	}

	public Optional<CovidCheck> getById(String id) {
		return covidCheckRepository.findById(id);
	}

	public void deleteById(String id) {
		covidCheckRepository.deleteById(id);
	}

}
