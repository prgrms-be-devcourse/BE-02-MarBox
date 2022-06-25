package prgrms.marco.be02marbox.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtConfigure(
	String header,
	String issuer,
	String clientSecret,
	int expirySeconds
) {
}
