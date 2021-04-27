package com.onegateafrica.Payloads.request;



import java.util.List;

import com.onegateafrica.Entities.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {

	private String phoneNumber;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private List<Role>  roles;

}