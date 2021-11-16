package com.spot.on.transactions;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class Controller {

	private final TestService service;

	@GetMapping("/test")
	public void test() {
		service.changeAnimalName_public();
	}

}


