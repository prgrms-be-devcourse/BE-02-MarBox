package prgrms.marco.be02marbox.domain.user.jwt;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import prgrms.marco.be02marbox.domain.user.Role;

public final class Jwt {

	private static final String EMAIL = "email";
	private static final String ROLE = "role";
	private static final String IAT = "iat";
	private static final String EXP = "exp";

	private final String issuer;
	private final String clientSecret;
	private final long expirySeconds;
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

	public String generateAccessToken(String email, Role role) {
		Date now = new Date();
		JWTCreator.Builder builder = getJwtBuilder(now);
		if (this.expirySeconds > 0) {
			builder.withExpiresAt(new Date(now.getTime() + this.expirySeconds));
		}

		Claims claims = Claims.from(email, role.name());
		builder.withClaim(EMAIL, claims.email)
			.withClaim(ROLE, claims.role);

		return builder.sign(this.algorithm);
	}

	public String generateRefreshToken() {
		Date now = new Date();
		JWTCreator.Builder builder = getJwtBuilder(now);
		if (this.expirySeconds > 0) {
			builder.withExpiresAt(new Date(now.getTime() + this.expirySeconds * 1_000));
		}

		return builder.sign(this.algorithm);
	}

	private JWTCreator.Builder getJwtBuilder(Date now) {
		JWTCreator.Builder builder = JWT.create();
		builder.withIssuer(this.issuer);
		builder.withIssuedAt(now);
		return builder;
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

	public long getExpirySeconds() {
		return this.expirySeconds;
	}

	public Algorithm getAlgorithm() {
		return this.algorithm;
	}

	public JWTVerifier getJwtVerifier() {
		return this.jwtVerifier;
	}

	public static class Claims {

		String email;
		String role;
		Date iat;
		Date exp;

		private Claims() {
		}

		public Claims(DecodedJWT decodedJwt) {
			Claim email = decodedJwt.getClaim(EMAIL);
			if (!email.isNull()) {
				this.email = email.asString();
			}
			Claim role = decodedJwt.getClaim(ROLE);
			if (!role.isNull()) {
				this.role = role.asString();
			}
			this.iat = decodedJwt.getIssuedAt();
			this.exp = decodedJwt.getExpiresAt();
		}

		public static Claims from(String email, String role) {
			Claims claims = new Claims();
			claims.email = email;
			claims.role = role;
			return claims;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append(EMAIL, this.email)
				.append(ROLE, this.role)
				.append(IAT, this.iat)
				.append(EXP, this.exp)
				.toString();
		}
	}
}
