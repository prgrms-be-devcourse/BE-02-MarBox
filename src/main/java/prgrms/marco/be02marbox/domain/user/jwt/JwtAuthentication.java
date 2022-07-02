package prgrms.marco.be02marbox.domain.user.jwt;

import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class JwtAuthentication {

	private final String token;
	private final String username;

	JwtAuthentication(String token, String username) {
		if (token.isEmpty()) {
			throw new IllegalArgumentException(EMPTY_TOKEN_EXP_MSG.getMessage());
		}
		if (username.isEmpty()) {
			throw new IllegalArgumentException(EMPTY_USERNAME_EXP_MSG.getMessage());
		}

		this.token = token;
		this.username = username;
	}

	public String getToken() {
		return this.token;
	}

	public String getUsername() {
		return this.username;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("token", this.token)
			.append("username", this.username)
			.toString();
	}
}
