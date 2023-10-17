package com.fakecoders.foundation.quickstart.dummy.service.impl;

import org.springframework.stereotype.Service;

import com.fakecoders.foundation.quickstart.dummy.service.IDummyService;

@Service
public class DummyServiceImpl implements IDummyService{

	@Override
	public String makePayment() {
		System.out.println("Amount Debited");
		
		
		
		System.out.println("Amount credited");
		return "this is dummy api";
	}

}
