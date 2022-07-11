package prgrms.marco.be02marbox.domain.user.jwt;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import prgrms.marco.be02marbox.config.JwtConfigure;
import prgrms.marco.be02marbox.domain.user.Role;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(JwtConfigure.class)
@TestPropertySource("classpath:jwt-config.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtTest {

	@Autowired
	private JwtConfigure jwtConfigure;

	private Jwt jwt;

	@BeforeAll
	void setUp() {
		jwt = new Jwt(jwtConfigure.issuer(), jwtConfigure.clientSecret(), jwtConfigure.expirySeconds());
	}

	@Test
	@DisplayName("access token 생성,검증 성공")
	void testSignAndVerifySuccess() {
		//given
		String email = "morgan@mail.com";
		Role role = Role.ROLE_ADMIN;
		String accessToken = jwt.generateAccessToken(email, role);

		//when
		Jwt.Claims claims = jwt.verify(accessToken);

		//then
		assertAll(
			() -> assertThat(claims.email).isEqualTo(email),
			() -> assertThat(claims.role).isEqualTo(role.name())
		);
	}

	@Test
	@DisplayName("refresh token 생성, 검증 성공")
	void testRefreshTokenGenerateAndVerifySuccess() {
		//given
		Jwt jwt = new Jwt(jwtConfigure.issuer(), jwtConfigure.clientSecret(), jwtConfigure.expirySeconds());
		String email = "morgan@mailcom";
		String refreshToken = jwt.generateRefreshToken(email);

		//when
		Jwt.Claims claims = jwt.verify(refreshToken);

		//then
		assertThat(claims.email).isEqualTo(email);
	}
}
