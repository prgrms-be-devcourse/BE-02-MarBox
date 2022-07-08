package prgrms.marco.be02marbox.domain.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refresh_token")
public class RefreshToken {

	@Id
	private String email;

	private String token;

	public RefreshToken(String email, String token) {
		this.email = email;
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public String getToken() {
		return token;
	}

	public void updateToken(String newToken) {
		this.token = newToken;
	}
}
