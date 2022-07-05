package prgrms.marco.be02marbox.domain.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.auth0.jwt.exceptions.JWTVerificationException;

import prgrms.marco.be02marbox.domain.user.RefreshToken;
import prgrms.marco.be02marbox.domain.user.Role;
import prgrms.marco.be02marbox.domain.user.User;
import prgrms.marco.be02marbox.domain.user.repository.RefreshTokenRepository;
import prgrms.marco.be02marbox.domain.user.repository.UserRepository;

@DataJpaTest
@Import(RefreshTokenService.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RefreshTokenServiceTest {

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	private User user;

	@BeforeAll
	void setUp() {
		user = userRepository.save(new User(
			"pang@mail.com",
			"encryptedPassword",
			"pang",
			Role.ROLE_ADMIN));
	}

	@AfterAll()
	void clean() {
		userRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("updateRefreshToken 성공 - 새로운 엔티티 생성")
	void testUpdateRefreshTokenSuccessMakingNewEntity() {
		//given when
		String token = "refresh-token";
		refreshTokenService.updateRefreshToken(user, token);

		//then
		RefreshToken refreshToken = refreshTokenRepository.findByUser(user).get();
		assertAll(
			() -> assertThat(refreshToken).isNotNull(),
			() -> assertThat(refreshToken.getUser().getEmail()).isEqualTo(user.getEmail()),
			() -> assertThat(refreshToken.getToken()).isEqualTo(token)
		);
	}

	@Test
	@DisplayName("updateRefreshToken 성공 - 기존 엔티티 업데이트")
	void testUpdateRefreshTokenSuccessUpdatingExistEntity() {
		//given
		RefreshToken oldRefreshToken = new RefreshToken(user, "old-refresh-token");
		refreshTokenRepository.save(oldRefreshToken);

		//when
		String newToken = "new-refresh-token";
		refreshTokenService.updateRefreshToken(user, newToken);

		//then
		RefreshToken refreshToken = refreshTokenRepository.findByUser(user).get();
		assertAll(
			() -> assertThat(refreshToken).isNotNull(),
			() -> assertThat(refreshToken.getUser().getEmail()).isEqualTo(user.getEmail()),
			() -> assertThat(refreshToken.getToken()).isEqualTo(newToken)
		);
	}

	@Test
	@DisplayName("findByToken 성공")
	void testFindByTokenSuccess() {
		//given
		RefreshToken refreshToken = new RefreshToken(user, "refresh-token");
		RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

		//when
		RefreshToken retrievedToken = refreshTokenService.findByToken(refreshToken.getToken());

		//then
		assertThat(retrievedToken.getId()).isEqualTo(savedRefreshToken.getId());
	}

	@Test
	@DisplayName("findByToken 실패 - 이미 갱신된 토큰")
	void testFindByTokenFailBecauseAlreadyUpdated() {
		//given
		String oldRefreshToken = "old-refresh-token";

		//when then
		assertThatThrownBy(() -> refreshTokenService.findByToken(oldRefreshToken))
			.isInstanceOf(JWTVerificationException.class)
			.hasMessageContaining(ALREADY_UPDATED_TOKEN_EXP_MSG.getMessage());
	}
}
