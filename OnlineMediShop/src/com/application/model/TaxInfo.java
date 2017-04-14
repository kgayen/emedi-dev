package com.application.model;

import java.util.HashMap;
import java.util.Map;

public class TaxInfo {
	
	private Map<String,Map<String,Double>> taxInfoMap = new HashMap<String,Map<String,Double>>();

	public Map<String,Map<String,Double>> getTaxInfoMap() {
		return taxInfoMap;
	}

	public void setTaxInfoMap(Map<String,Map<String,Double>> taxInfoMap) {
		this.taxInfoMap = taxInfoMap;
	}
}