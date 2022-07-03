package prgrms.marco.be02marbox.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.user.User;
import prgrms.marco.be02marbox.domain.user.dto.ResponseLoginToken;
import prgrms.marco.be02marbox.domain.user.jwt.Jwt;

@Service
@Transactional(readOnly = true)
public class JwtService {

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
	public ResponseLoginToken authenticateUser(String email, String password) {

		//사용자 인증
		User user = userService.login(email, password);

		//토큰 생성
		String accessToken = jwt.generateAccessToken(user.getName(), user.getRole());
		String refreshToken = jwt.generateRefreshToken();

		refreshTokenService.updateRefreshToken(user, refreshToken);

		return new ResponseLoginToken(accessToken, refreshToken);
	}

	public ResponseLoginToken refreshToken(String accessToken, String refreshToken) {
		return null;
	}
}
