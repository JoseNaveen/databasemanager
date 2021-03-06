package com.joey.databasemanager.converters;

import org.springframework.core.convert.converter.Converter;

import com.joey.databasemanager.beans.Gender;

public class StringToEnumConvertor implements Converter<String, Gender>{

	@Override
	public Gender convert(String source) {
		if (source.equals("Male")) {
			return Gender.MALE;
		}
		else if (source.equals("Female")) {
			return Gender.FEMALE;
		}
		else {
			return Gender.OTHER;
		}
	}

}
