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

import com.auth0.jwt.exceptions.TokenExpiredException;

import prgrms.marco.be02marbox.domain.user.RefreshToken;
import prgrms.marco.be02marbox.domain.user.Role;
import prgrms.marco.be02marbox.domain.user.User;
import prgrms.marco.be02marbox.domain.user.dto.ResponseJwtToken;
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
		given(jwt.generateAccessToken(user.getEmail(), user.getRole())).willReturn(accessToken);
		given(jwt.generateRefreshToken()).willReturn(refreshToken);

		//when
		ResponseJwtToken responseJwtToken = jwtService.authenticateUser(user.getEmail(), rawPassword);

		//then
		assertAll(
			() -> assertThat(responseJwtToken.accessToken()).isEqualTo(accessToken),
			() -> assertThat(responseJwtToken.refreshToken()).isEqualTo(refreshToken)
		);
	}

	@Test
	@DisplayName("refreshToken 성공")
	void testRefreshTokenSuccess() {
		//given
		String accessToken = "access-token";
		given(jwt.verify(accessToken)).willThrow(new TokenExpiredException("만료기간이 끝난 access token 입니다."));

		User user = new User(
			"pang@mail.com",
			"encrypted",
			"pang",
			Role.ROLE_ADMIN);
		String refreshToken = "refresh-token";
		RefreshToken validRefreshToken = new RefreshToken(user, refreshToken);
		given(refreshTokenService.findByToken(refreshToken)).willReturn(validRefreshToken);

		String newAccessToken = "new-access-token";
		String newRefreshToken = "new-refresh-token";
		given(jwt.generateAccessToken(user.getEmail(), user.getRole())).willReturn(newAccessToken);
		given(jwt.generateRefreshToken()).willReturn(newRefreshToken);

		//when
		ResponseJwtToken responseJwtToken = jwtService.refreshToken(accessToken, refreshToken);

		//then
		assertAll(
			() -> assertThat(responseJwtToken.accessToken()).isEqualTo(newAccessToken),
			() -> assertThat(responseJwtToken.refreshToken()).isEqualTo(newRefreshToken));
	}
}
