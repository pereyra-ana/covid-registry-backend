package com.ana.covidregistry.model;

public class Stats {

	private Long healthy;
	private Long infected;
	private Long immune;
	
	public Stats() {
		this.healthy = 0l;
		this.infected = 0l;
		this.immune = 0l;
	}

	public Long getHealthy() {
		return healthy;
	}

	public void setHealthy(Long healthy) {
		this.healthy = healthy;
	}

	public Long getInfected() {
		return infected;
	}

	public void setInfected(Long infected) {
		this.infected = infected;
	}

	public Long getImmune() {
		return immune;
	}

	public void setImmune(Long immune) {
		this.immune = immune;
	}
}
