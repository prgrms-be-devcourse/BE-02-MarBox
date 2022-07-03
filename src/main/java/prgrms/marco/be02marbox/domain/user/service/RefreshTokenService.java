package prgrms.marco.be02marbox.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public void updateRefreshToken(User user, String refreshToken) {
		refreshTokenRepository.findByUser(user)
			.ifPresentOrElse(token -> token.updateToken(refreshToken),
				() -> refreshTokenRepository.save(new RefreshToken(user, refreshToken)));
	}
}
