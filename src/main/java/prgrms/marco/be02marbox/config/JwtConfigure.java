package prgrms.marco.be02marbox.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SpringSecurity 컴포넌트 만들기
 * 사용자 로그인 Controller 개발하기
 * 사용자 로그인 Service 개발하기
 */
@ConfigurationProperties(prefix = "jwt")
public record JwtConfigure(
	String header,
	String issuer,
	String clientSecret,
	int expirySeconds
) {
}
