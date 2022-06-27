package prgrms.marco.be02marbox.domain.user.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public final class Jwt {

	private final String issuer;
	private final String clientSecret;
	private final int expirySeconds;
	private final Algorithm algorithm;
	private final JWTVerifier jwtVerifier;

	public Jwt(String issuer, String clientSecret, int expirySeconds) {
		this.issuer = issuer;
		this.clientSecret = clientSecret;
		this.expirySeconds = expirySeconds;
		this.algorithm = Algorithm.HMAC512(clientSecret);
		this.jwtVerifier = com.auth0.jwt.JWT.require(this.algorithm)
			.withIssuer(issuer)
			.build();
	}

	public String sign(Claims claims) {
		Date now = new Date();
		JWTCreator.Builder builder = JWT.create();
		builder.withIssuer(this.issuer)
			.withIssuedAt(now);
		if (this.expirySeconds > 0) {
			builder.withExpiresAt(new Date(now.getTime() + this.expirySeconds * 1_000L));
		}
		return builder.withClaim("username", claims.username)
			.withClaim("role", claims.role)
			.sign(this.algorithm);
	}

	public Claims verify(String token) {
		return new Claims(this.jwtVerifier.verify(token));
	}

	public String getIssuer() {
		return this.issuer;
	}

	public String getClientSecret() {
		return this.clientSecret;
	}

	public int getExpirySeconds() {
		return this.expirySeconds;
	}

	public Algorithm getAlgorithm() {
		return this.algorithm;
	}

	public JWTVerifier getJwtVerifier() {
		return this.jwtVerifier;
	}

	public static class Claims {

		String username;
		String role;
		Date iat;
		Date exp;

		private Claims() {
		}

		public Claims(DecodedJWT decodedJwt) {
			Claim username = decodedJwt.getClaim("username");
			if (!username.isNull()) {
				this.username = username.asString();
			}
			Claim role = decodedJwt.getClaim("role");
			if (!role.isNull()) {
				this.role = role.asString();
			}
			this.iat = decodedJwt.getIssuedAt();
			this.exp = decodedJwt.getExpiresAt();
		}

		public static Claims from(String username, String role) {
			Claims claims = new Claims();
			claims.username = username;
			claims.role = role;
			return claims;
		}

		public Map<String, Object> asMap() {
			Map<String, Object> map = new HashMap<>();
			map.put("username", username);
			map.put("roles", role);
			map.put("iat", iat());
			map.put("exp", exp());
			return map;
		}

		long iat() {
			return this.iat != null ? this.iat.getTime() : -1;
		}

		long exp() {
			return this.exp != null ? this.exp.getTime() : -1;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("username", this.username)
				.append("role", this.role)
				.append("iat", this.iat)
				.append("exp", this.exp)
				.toString();
		}
	}
}
