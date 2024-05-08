package rw.qt.userms.security.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationResponse {
	private String accessToken;

	private String tokenType;

	private String refreshToken;

	public JwtAuthenticationResponse(String token) {
		this.accessToken = token;
	}

}