package prgrms.marco.be02marbox.domain.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import prgrms.marco.be02marbox.domain.user.Role;
import prgrms.marco.be02marbox.domain.user.User;
import prgrms.marco.be02marbox.domain.user.dto.ResponseLoginToken;
import prgrms.marco.be02marbox.domain.user.jwt.Jwt;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

	@Mock
	private UserService userService;

	@Mock
	private RefreshTokenService refreshTokenService;

	@Mock
	private Jwt jwt;

	private JwtService jwtService;

	@BeforeEach
	void setUp() {
		jwtService = new JwtService(userService, refreshTokenService, jwt);
	}

	@Test
	@DisplayName("authenticateUser 성공")
	void testAuthenticateUserSuccess() {
		//given
		User user = new User(
			"pang@mail.com",
			"encrypted",
			"pang",
			Role.ROLE_ADMIN);
		String rawPassword = "1234";
		given(userService.login(user.getEmail(), rawPassword)).willReturn(user);

		String accessToken = "access-token";
		String refreshToken = "refresh-token";
		given(jwt.generateAccessToken(user.getName(), user.getRole())).willReturn(accessToken);
		given(jwt.generateRefreshToken()).willReturn(refreshToken);

		//when
		ResponseLoginToken responseLoginToken = jwtService.authenticateUser(user.getEmail(), rawPassword);

		//then
		assertAll(
			() -> assertThat(responseLoginToken.accessToken()).isEqualTo(accessToken),
			() -> assertThat(responseLoginToken.refreshToken()).isEqualTo(refreshToken)
		);
	}
}