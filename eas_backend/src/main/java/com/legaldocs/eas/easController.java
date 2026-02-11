package com.legaldocs.eas;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class easController {
	
	@GetMapping("/")
	public String home() {
		return "Home page";
	}
}
