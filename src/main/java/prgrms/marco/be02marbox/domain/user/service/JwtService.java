package prgrms.marco.be02marbox.domain.user.service;

import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.exceptions.TokenExpiredException;

import prgrms.marco.be02marbox.domain.user.RefreshToken;
import prgrms.marco.be02marbox.domain.user.User;
import prgrms.marco.be02marbox.domain.user.dto.ResponseJwtToken;
import prgrms.marco.be02marbox.domain.user.jwt.Jwt;

@Service
@Transactional(readOnly = true)
public class JwtService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final UserService userService;
	private final RefreshTokenService refreshTokenService;
	private final Jwt jwt;

	public JwtService(UserService userService, RefreshTokenService refreshTokenService, Jwt jwt) {
		this.userService = userService;
		this.refreshTokenService = refreshTokenService;
		this.jwt = jwt;
	}

	/**
	 * 이메일과 비밀번호를 받아, 사용자를 인증하고 토큰을 발급해준다.
	 * @param email
	 * @param password
	 * @return ResponseLoginToken
	 * @throws @throws InvalidEmailException 이메일이 DB에 존재하지 않는 경우
	 * @throws org.springframework.security.authentication.BadCredentialsException 비밀번호가 틀린 경우
	 */
	@Transactional
	public ResponseJwtToken authenticateUser(String email, String password) {

		//사용자 인증
		User user = userService.login(email, password);

		//토큰 생성
		String accessToken = jwt.generateAccessToken(user.getName(), user.getRole());
		String refreshToken = jwt.generateRefreshToken();

		refreshTokenService.updateRefreshToken(user, refreshToken);

		return new ResponseJwtToken(accessToken, refreshToken);
	}

	/**
	 * access token, refresh token 재발급한다.
	 * @param accessToken
	 * @param refreshToken
	 * @return JWTVerificationException 유효하지 않은 access token or refresh token
	 */
	@Transactional
	public ResponseJwtToken refreshToken(String accessToken, String refreshToken) {
		validateAccessTokenExpired(accessToken);

		RefreshToken validRefreshToken = validateRefreshToken(refreshToken);

		String newAccessToken = jwt.generateAccessToken(
			validRefreshToken.getUser().getName(), validRefreshToken.getUser().getRole());
		validRefreshToken.updateToken(jwt.generateRefreshToken());

		return new ResponseJwtToken(newAccessToken, validRefreshToken.getToken());
	}

	private void validateAccessTokenExpired(String accessToken) {
		try {
			jwt.verify(accessToken);
		} catch (TokenExpiredException exception) {
			log.debug("access-token 기간 만료");
			return;
		}
		throw new IllegalArgumentException(VALID_ACCESS_TOKEN_EXP_MSG.getMessage());
	}

	private RefreshToken validateRefreshToken(String refreshToken) {
		jwt.verify(refreshToken);

		return refreshTokenService.findByToken(refreshToken);
	}
}
