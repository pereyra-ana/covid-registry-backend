package com.ana.covidregistry.enums;

public enum ResultEnum {
	HEALTY("Sano"),
	INFECTED("Infectado"),
	IMMUNE("Inmune");

	private String result;
	 
	ResultEnum(String result) {
        this.result = result;
    }
 
    public String getResult() {
        return result;
    }

}
