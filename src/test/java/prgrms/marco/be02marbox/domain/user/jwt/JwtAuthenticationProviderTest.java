package prgrms.marco.be02marbox.domain.user.jwt;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import prgrms.marco.be02marbox.config.JwtConfigure;
import prgrms.marco.be02marbox.domain.user.dto.ResponseLoginUser;
import prgrms.marco.be02marbox.domain.user.service.UserService;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(JwtConfigure.class)
@TestPropertySource("classpath:jwt-config.properties")
class JwtAuthenticationProviderTest {

	@Autowired
	private Jwt jwt;

	@Mock
	private UserService userService;

	@Test
	@DisplayName("인증 처리 성공")
	void testAuthenticateSuccess() {
		//given
		String email = "pang@mail.com";
		String password = "1234";
		ResponseLoginUser responseLoginUser = new ResponseLoginUser("pang", "ROLE_ADMIN");
		given(userService.login(email, password)).willReturn(responseLoginUser);

		JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(jwt, userService);

		//when
		JwtAuthenticationToken authentication = (JwtAuthenticationToken)jwtAuthenticationProvider
			.authenticate(new JwtAuthenticationToken(email, password));

		//then
		JwtAuthentication principal = (JwtAuthentication)authentication.getPrincipal();
		assertAll(
			() -> assertThat(authentication.isAuthenticated()).isTrue(),
			() -> assertThat(authentication.getCredentials()).isNull(),
			() -> assertThat(principal.getToken()).isNotBlank(),
			() -> assertThat(principal.getUsername()).isEqualTo(responseLoginUser.name())
		);
	}

	@TestConfiguration
	static class Config {
		@Bean
		public Jwt jwt(JwtConfigure jwtConfigure) {
			return new Jwt(
				jwtConfigure.issuer(),
				jwtConfigure.clientSecret(),
				jwtConfigure.expirySeconds());
		}
	}
}
