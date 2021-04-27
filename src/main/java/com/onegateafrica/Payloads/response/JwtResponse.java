package com.onegateafrica.Payloads.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponse {
	private Long id;
	private String token;
	private String username;
	private List<String> authorities;
	private String type = "Bearer";

	public JwtResponse(String accessToken, Long id, String username, List<String> authorities) {
		this.id = id;
		this.token = accessToken;
		this.username = username;
		this.authorities = authorities;
	}
}