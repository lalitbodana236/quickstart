package com.fakecoders.foundation.quickstart.dummy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/dummy")
public class DummyController {
	
	@GetMapping("/")
	public String dummy() {
		return "this is dummy api";
	}
}
