package prgrms.marco.be02marbox.domain.user.service;

import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import prgrms.marco.be02marbox.domain.exception.custom.user.InvalidEmailException;
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
	 * @return ResponseJwtToken
	 * @throws InvalidEmailException 이메일이 DB에 존재하지 않는 경우
	 * @throws BadCredentialsException 비밀번호가 틀린 경우
	 */
	@Transactional
	public ResponseJwtToken authenticateUser(String email, String password) {
		User user = userService.login(email, password);

		String accessToken = jwt.generateAccessToken(user.getEmail(), user.getRole());
		String refreshToken = jwt.generateRefreshToken(user.getEmail());

		refreshTokenService.updateRefreshToken(new RefreshToken(user.getEmail(), refreshToken));

		return new ResponseJwtToken(accessToken, refreshToken);
	}

	/**
	 * access token, refresh token 재발급한다.
	 * @param accessToken
	 * @param refreshToken
	 * @return ResponseJwtToken
	 * @throws JWTVerificationException 유효하지 않은 access token or refresh token
	 * @throws InvalidEmailException 유요하지 않은 email
	 */
	public ResponseJwtToken refreshToken(String accessToken, String refreshToken) {
		validateAccessTokenExpired(accessToken);
		RefreshToken validRefreshToken = validateRefreshToken(refreshToken);

		User user = userService.findByEmail(validRefreshToken.getEmail());

		String newAccessToken = jwt.generateAccessToken(user.getEmail(), user.getRole());
		validRefreshToken.updateToken(jwt.generateRefreshToken(user.getEmail()));

		refreshTokenService.updateRefreshToken(validRefreshToken);

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
		Jwt.Claims claims = jwt.verify(refreshToken);

		RefreshToken token = refreshTokenService.findByEmail(claims.getEmail());
		if (!token.getToken().equals(refreshToken)) {
			throw new JWTVerificationException(ALREADY_UPDATED_TOKEN_EXP_MSG.getMessage());
		}
		return token;
	}
}
