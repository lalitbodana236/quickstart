package com.fakecoders.foundation.quickstart.sample;


import io.swagger.v3.oas.annotations.Hidden;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fakecoders.foundation.quickstart.model.User;
import com.fakecoders.foundation.quickstart.service.IUserService;

@RestController
@RequestMapping("/api/v1/demo-controller")
//@Hidden
//@CrossOrigin(origins = "http://localhost:4200")
public class DemoController {
	
	@Autowired
	private IUserService userService;

  @GetMapping
  public ResponseEntity<String> sayHello() {
    return ResponseEntity.ok("Hello from secured endpoint");
  }
  
  @GetMapping("/user")
  public List<User> getAllUser(){
	  
	  return userService.findAllUser();
  }

}
