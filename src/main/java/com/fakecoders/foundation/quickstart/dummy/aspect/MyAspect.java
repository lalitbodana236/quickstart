package com.fakecoders.foundation.quickstart.dummy.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {
	
	@Before("execution(* com.fakecoders.foundation.quickstart.dummy.service.impl.DummyServiceImpl.makePayment())")
	public void printBefore() {
		System.out.println("Payment started...");
	}
}
