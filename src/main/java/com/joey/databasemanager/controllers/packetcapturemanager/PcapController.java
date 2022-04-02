package com.joey.databasemanager.controllers.packetcapturemanager;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PcapController {
	
	
	@PostMapping("/packetcaptures/start")
	public ResponseEntity<String> startCapture() {
		
		
		
		return ResponseEntity.ok("Success");
	}

}
