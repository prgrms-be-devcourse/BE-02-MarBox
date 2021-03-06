package prgrms.marco.be02marbox.domain.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import prgrms.marco.be02marbox.config.QueryDslConfig;
import prgrms.marco.be02marbox.domain.exception.custom.user.DuplicateEmailException;
import prgrms.marco.be02marbox.domain.exception.custom.user.InvalidEmailException;
import prgrms.marco.be02marbox.domain.user.Role;
import prgrms.marco.be02marbox.domain.user.User;
import prgrms.marco.be02marbox.domain.user.repository.UserRepository;

@DataJpaTest
@Import({UserService.class, BCryptPasswordEncoder.class, QueryDslConfig.class})
class UserServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	@DisplayName("사용자 생성 성공")
	void testSaveSuccess() {
		//given when
		Long pangId = userService.create(
			"pang@mail.com",
			"1234",
			"pang",
			Role.ROLE_CUSTOMER);

		//then
		Optional<User> user = userRepository.findById(pangId);
		assertAll(
			() -> assertThat(user).isNotEmpty(),
			() -> assertThat(user.get().getId()).isEqualTo(pangId)
		);
	}

	@Test
	@DisplayName("사용자 생성 실패 - 이메일 중복")
	void testSaveFailBecauseDuplicateEmail() {
		//given
		String duplicateEmail = "pang@mail.com";
		User user = new User(
			duplicateEmail,
			"1234",
			"pang",
			Role.ROLE_CUSTOMER);
		userRepository.save(user);

		//when then
		assertThatThrownBy(() -> userService.create(
			duplicateEmail,
			"1234",
			"bang",
			Role.ROLE_CUSTOMER))
			.isInstanceOf(DuplicateEmailException.class)
			.hasMessageContaining("이미 존재하는 이메일 입니다.");
	}

	@Test
	@DisplayName("사용자 로그인 성공")
	void testLoginSuccess() {
		//given
		String rawPassword = "1234";
		User user = new User(
			"pang@mail.com",
			passwordEncoder.encode(rawPassword),
			"pang",
			Role.ROLE_ADMIN);
		User savedUser = userRepository.save(user);

		//when
		User authenticatedUser = userService.login(savedUser.getEmail(), rawPassword);

		//then
		assertAll(
			() -> assertThat(authenticatedUser.getId()).isEqualTo(savedUser.getId()),
			() -> assertThat(authenticatedUser.getEmail()).isEqualTo(savedUser.getEmail()),
			() -> assertThat(authenticatedUser.getPassword()).isEqualTo(savedUser.getPassword())
		);
	}

	@Test
	@DisplayName("사용자 로그인 실패 - 존재 하지 않는 이메일")
	void testLoginFailBecauseInvalidEmail() {
		//given
		String email = "invalid@mail.com";
		String password = "1234";

		//when then
		assertThatThrownBy(() -> userService.login(email, password))
			.isInstanceOf(InvalidEmailException.class)
			.hasMessageContaining(INVALID_EMAIL_EXP_MSG.getMessage());
	}

	@Test
	@DisplayName("사용자 로그인 실패 - 비밀번호 틀림")
	void testLoginFailBecauseWrongPassword() {
		//given
		User user = new User(
			"pang@mail.com",
			"1234",
			"pang",
			Role.ROLE_ADMIN);
		User savedUser = userRepository.save(user);

		String wrongPassword = "7777";

		//when then
		assertThatThrownBy(() -> userService.login(savedUser.getEmail(), wrongPassword))
			.isInstanceOf(BadCredentialsException.class)
			.hasMessageContaining(WRONG_PASSWORD_EXP_MSG.getMessage());
	}
}
