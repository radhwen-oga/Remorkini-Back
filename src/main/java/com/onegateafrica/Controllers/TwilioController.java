package com.onegateafrica.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import com.onegateafrica.Entities.TwilioVerification;
import com.onegateafrica.ServiceImpl.PhoneverificationServiceImpl;
import com.onegateafrica.Service.VerificationResult;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class TwilioController {
	@Autowired
	PhoneverificationServiceImpl phonesmsservice;
	    
	@RequestMapping("/")
	public String homepage(ModelAndView model)
	{
		return "index";
	}
	
	@PostMapping("/sendotp")
	public ResponseEntity<String> sendotp(@RequestBody String PhoneNumber)
	{
	    VerificationResult result=phonesmsservice.startVerification(PhoneNumber);
	    if(result.isValid())
	    {
	    	return new ResponseEntity<>("Otp Sent..",HttpStatus.OK);
	    }
		return new ResponseEntity<>("Otp failed to sent..",HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/verifyotp")
	public ResponseEntity<String> verifyOtp(@RequestBody TwilioVerification ob)
	{
		
	    VerificationResult result=phonesmsservice.checkverification	(ob.getPhoneNumber(),ob.getOtp());
	    if(result.isValid())
	    {
	    	return new ResponseEntity<>("Your number is Verified",HttpStatus.OK);
	    }
		return new ResponseEntity<>("Something wrong/ Otp incorrect",HttpStatus.BAD_REQUEST);
	}

}
