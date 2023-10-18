package com.fakecoders.foundation.quickstart.dummy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fakecoders.foundation.quickstart.dummy.service.IDummyService;


@RestController
@RequestMapping("/api/dummy")
public class DummyController {
	
	
	
	IDummyService iDummyService;
	
	
	@Autowired
	public DummyController(IDummyService iDummyService) {
		super();
		this.iDummyService = iDummyService;
	}



	@GetMapping("/")
	public String dummy() {
		return iDummyService.makePayment();
		
	}
	
	@GetMapping("/test")
	public String test() {
		return "this is dummy api";
		
	}
}
