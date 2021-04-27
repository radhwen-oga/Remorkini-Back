package com.onegateafrica.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TwilioVerification {
	private String phoneNumber;
	private String otp;
}
