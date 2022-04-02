package com.joey.databasemanager.unitconverter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConversionController {
	
	@GetMapping("/api/v1/weightconverter")
	public ResponseEntity<ConversionResponse> convert(@RequestParam WeightUnit srcunit,
			@RequestParam WeightUnit tgtunit, @RequestParam double value) {
		var resp = new ConversionResponse();
		resp.setValue(srcunit.convert(srcunit, tgtunit, value));
		resp.setUnit(tgtunit);
		return new ResponseEntity<>(resp, new HttpHeaders(),HttpStatus.OK);
		
		
	}
}
