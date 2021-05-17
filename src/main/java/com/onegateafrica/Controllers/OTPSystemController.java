package com.onegateafrica.Controllers;

import com.onegateafrica.Entities.Consommateur;
import com.onegateafrica.Entities.OTPSystem;
import com.onegateafrica.Service.ConsommateurService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/")
@CrossOrigin(origins = "*")
public class OTPSystemController {

    private Map<String, OTPSystem> otpData = new HashMap<>();
    private static final String ACCOUNT_SID = "ACe2acbe4cfee59f88ff60e1df94d8c739";
    private static final String AUTH_ID = "c2c1891ca777a2c934774605504ad57f";
    private final ConsommateurService consommateurService;

    @Autowired
    public OTPSystemController(ConsommateurService consommateurService) {
        this.consommateurService = consommateurService;
    }

    static {
        Twilio.init(ACCOUNT_SID, AUTH_ID);
    }

    @PostMapping("/sendOTP")
    public Boolean sendOTP(@RequestParam("phoneNumberInput") String phoneNumberInput) {

        if (phoneNumberInput != null) {
            Consommateur consommateur = consommateurService.getConsommateurByPhoneNumber(phoneNumberInput);
            if (consommateur == null) {
                OTPSystem otpSystem = new OTPSystem();
                otpSystem.setPhoneNumber(phoneNumberInput);
                otpSystem.setOtp(String.valueOf(((int) (Math.random() * (10000 - 1000))) + 1000));
                otpSystem.setExpireTime(System.currentTimeMillis() + 60000);
                otpData.put(phoneNumberInput, otpSystem);
                Message.creator(new PhoneNumber("+216" + phoneNumberInput), new PhoneNumber("+14053379099"),
                        "Your OTP is: " + otpSystem.getOtp()).create();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @PostMapping("verifyOTP")
    ResponseEntity<String> verifyOtp(@RequestBody OTPSystem requestBodyOTPSystem) {
        System.out.println(requestBodyOTPSystem.getOtp());
        System.out.println(requestBodyOTPSystem.getPhoneNumber());
        if (requestBodyOTPSystem == null || requestBodyOTPSystem.getOtp() == null
                || requestBodyOTPSystem.getPhoneNumber() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not enough provided information");
        } else if (otpData.containsKey(requestBodyOTPSystem.getPhoneNumber())) {
            OTPSystem otpSystem = otpData.get(requestBodyOTPSystem.getPhoneNumber());
          System.out.println(otpSystem.getPhoneNumber()+" "+otpSystem.getOtp()+" "+ otpSystem.getExpireTime());
          System.out.println(System.currentTimeMillis());
          System.out.println(otpSystem.getExpireTime() >= System.currentTimeMillis());
          System.out.print(otpSystem.getOtp() == requestBodyOTPSystem.getOtp());
            if (otpSystem.getExpireTime() >= System.currentTimeMillis() && otpSystem.getOtp().equals(requestBodyOTPSystem.getOtp())) {
                return ResponseEntity.status(HttpStatus.OK).body("Verified");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
    }

  @PutMapping("verifyOTPSignIn")
  public Object verifyOtpSignIn (@RequestBody OTPSystem requestBodyOTPSystem) {

    if (requestBodyOTPSystem==null  || requestBodyOTPSystem.getOtp()==null
        || requestBodyOTPSystem.getPhoneNumber()==null)
    {
      return "null";
    }
    else if (otpData.containsKey(requestBodyOTPSystem.getPhoneNumber())) {
      OTPSystem otpSystem = otpData.get(requestBodyOTPSystem.getPhoneNumber());
      if (otpSystem != null) {
        if (otpSystem.getExpireTime() >= System.currentTimeMillis()) {
          if (requestBodyOTPSystem.getOtp().equals(otpSystem.getOtp())) {
            otpData.remove(requestBodyOTPSystem.getPhoneNumber());
            return consommateurService.getConsommateurByPhoneNumber(requestBodyOTPSystem.getPhoneNumber());
          }
          return "null";
        }
        return "null";
      }
      return "null";
    }
    return "null";

  }
}
