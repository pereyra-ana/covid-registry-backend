package com.ana.covidregistry.enums;

public enum ChainLetterEnum {
	A("A"), C("C"), G("G"), T("T");

	private String value;

	ChainLetterEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
