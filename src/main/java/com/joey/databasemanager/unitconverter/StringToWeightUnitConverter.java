package com.joey.databasemanager.unitconverter;

import org.springframework.core.convert.converter.Converter;

public class StringToWeightUnitConverter implements Converter<String, WeightUnit>{

	@Override
	public WeightUnit convert(String source) {
		return WeightUnit.valueOf(source);
	}

}
