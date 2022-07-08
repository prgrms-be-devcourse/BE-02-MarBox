package prgrms.marco.be02marbox.domain.user.service;

import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import org.springframework.stereotype.Service;

import com.auth0.jwt.exceptions.JWTVerificationException;

import prgrms.marco.be02marbox.domain.user.RefreshToken;
import prgrms.marco.be02marbox.domain.user.repository.RefreshTokenRedisRepository;

@Service
public class RefreshTokenService {

	private final RefreshTokenRedisRepository refreshTokenRedisRepository;

	public RefreshTokenService(RefreshTokenRedisRepository refreshTokenRedisRepository) {
		this.refreshTokenRedisRepository = refreshTokenRedisRepository;
	}

	/**
	 * refresh token 갱신
	 * @param refreshTokenRedis
	 */
	public void updateRefreshToken(RefreshToken refreshTokenRedis) {
		refreshTokenRedisRepository.save(refreshTokenRedis);
	}

	/**
	 * 특정 email의 할당된 refresh token 찾기
	 * @param email
	 * @return RefreshTokenRedis
	 * @throws JWTVerificationException
	 */
	public RefreshToken findByEmail(String email) {
		return refreshTokenRedisRepository.findById(email)
			.orElseThrow(() -> new JWTVerificationException(INVALID_REFRESH_TOKEN_EXP_MSG.getMessage()));
	}
}
