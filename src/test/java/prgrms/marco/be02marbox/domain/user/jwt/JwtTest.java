package prgrms.marco.be02marbox.domain.user.jwt;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import prgrms.marco.be02marbox.config.JwtConfigure;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(JwtConfigure.class)
@TestPropertySource("classpath:jwt-config.properties")
class JwtTest {

	@Autowired
	private JwtConfigure jwtConfigure;

	@Test
	@DisplayName("jwt 생성,검증 성공")
	void testSignAndVerifySuccess() {
		//given
		Jwt jwt = new Jwt(jwtConfigure.issuer(), jwtConfigure.clientSecret(), jwtConfigure.expirySeconds());

		String username = "pang";
		String role = "ROLE_ADMIN";
		String token = jwt.sign(Jwt.Claims.from(username, role));

		//when
		Jwt.Claims claims = jwt.verify(token);

		//then
		System.out.println(claims);
		assertAll(
			() -> assertThat(claims.username).isEqualTo(username),
			() -> assertThat(claims.role).isEqualTo(role)
		);
	}
}
