package com.onegateafrica.Payloads.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateForm {
	private Long id;
	private String phoneNumber;
	private String password;
	private String firstName;
	private String lastName;
	private String email;



}
