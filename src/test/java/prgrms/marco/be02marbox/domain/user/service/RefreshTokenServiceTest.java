package prgrms.marco.be02marbox.domain.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;

import com.auth0.jwt.exceptions.JWTVerificationException;

import prgrms.marco.be02marbox.config.QueryDslConfig;
import prgrms.marco.be02marbox.domain.user.RefreshToken;
import prgrms.marco.be02marbox.domain.user.repository.RefreshTokenRedisRepository;

@DataRedisTest
@Import({RefreshTokenService.class})
class RefreshTokenServiceTest {

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private RefreshTokenRedisRepository refreshTokenRedisRepository;

	@AfterEach()
	void clean() {
		this.refreshTokenRedisRepository.deleteAll();
	}

	@Test
	@DisplayName("updateRefreshToken 성공 - 새로운 엔티티 생성")
	void testUpdateRefreshTokenSuccessMakingNewEntity() {
		//given
		long beforeUpdateCount = refreshTokenRedisRepository.count();
		RefreshToken token = new RefreshToken("hyuk@mail.com", "refresh-token");

		//when
		refreshTokenService.updateRefreshToken(token);

		//then
		assertThat(refreshTokenRedisRepository.count()).isEqualTo(beforeUpdateCount + 1);
	}

	@Test
	@DisplayName("updateRefreshToken 성공 - 기존 엔티티 업데이트")
	void testUpdateRefreshTokenSuccessUpdatingExistEntity() {
		//given
		RefreshToken token = new RefreshToken("hyuk@mail.com", "old-refresh-token");
		this.refreshTokenRedisRepository.save(token);

		long beforeUpdateCount = this.refreshTokenRedisRepository.count();

		String newToken = "new-refresh-token";
		token.updateToken(newToken);

		//when
		this.refreshTokenService.updateRefreshToken(token);

		//then
		RefreshToken refreshToken = this.refreshTokenRedisRepository.findById(token.getEmail()).get();
		assertAll(
			() -> assertThat(this.refreshTokenRedisRepository.count()).isEqualTo(beforeUpdateCount),
			() -> assertThat(refreshToken).isNotNull(),
			() -> assertThat(refreshToken.getEmail()).isEqualTo(token.getEmail()),
			() -> assertThat(refreshToken.getToken()).isEqualTo(token.getToken()));
	}

	@Test
	@DisplayName("findById 성공")
	void testFindByIdSuccess() {
		//given
		RefreshToken refreshToken = new RefreshToken("hyuk@mail.com", "refresh-token");
		this.refreshTokenRedisRepository.save(refreshToken);

		//when
		RefreshToken retrievedToken = this.refreshTokenService.findByEmail(refreshToken.getEmail());

		//then
		assertAll(
			() -> assertThat(retrievedToken.getEmail()).isEqualTo(refreshToken.getEmail()),
			() -> assertThat(retrievedToken.getToken()).isEqualTo(refreshToken.getToken()));
	}

	@Test
	@DisplayName("findById 실패 - 존재하지 않음")
	void testFindByIdFailBecauseNotExists() {
		//given when then
		assertThatThrownBy(() -> this.refreshTokenService.findByEmail("invalid@mail.com"))
			.isInstanceOf(JWTVerificationException.class)
			.hasMessageContaining(INVALID_REFRESH_TOKEN_EXP_MSG.getMessage());
	}
}
