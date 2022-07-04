package prgrms.marco.be02marbox.domain.user.service;

import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.exceptions.JWTVerificationException;

import prgrms.marco.be02marbox.domain.user.RefreshToken;
import prgrms.marco.be02marbox.domain.user.User;
import prgrms.marco.be02marbox.domain.user.repository.RefreshTokenRepository;

@Service
@Transactional(readOnly = true)
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
		this.refreshTokenRepository = refreshTokenRepository;
	}

	/**
	 * refreshToken 갱신 (존재하면 수정, 없으면 만들기)
	 * @param user
	 * @param refreshToken
	 */
	@Transactional
	public void updateRefreshToken(User user, String refreshToken) {
		refreshTokenRepository.findByUser(user)
			.ifPresentOrElse(token -> token.updateToken(refreshToken),
				() -> refreshTokenRepository.save(new RefreshToken(user, refreshToken)));
	}

	/**
	 * 특정 refreshToken의 refreshToken 엔티티를 찾는다.
	 * @param refreshToken
	 * @return
	 * @throws JWTVerificationException 이미 갱신된 refresh token
	 */
	public RefreshToken findByToken(String refreshToken) {
		return refreshTokenRepository.findByToken(refreshToken)
			.orElseThrow(() -> new JWTVerificationException(ALREADY_UPDATED_TOKEN_EXP_MSG.getMessage()));
	}
}
