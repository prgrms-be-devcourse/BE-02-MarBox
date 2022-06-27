package prgrms.marco.be02marbox.domain.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static prgrms.marco.be02marbox.domain.user.exception.Message.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.BadCredentialsException;

import prgrms.marco.be02marbox.domain.user.Role;
import prgrms.marco.be02marbox.domain.user.User;
import prgrms.marco.be02marbox.domain.user.dto.ResponseLoginUser;
import prgrms.marco.be02marbox.domain.user.exception.DuplicateEmailException;
import prgrms.marco.be02marbox.domain.user.exception.DuplicateNameException;
import prgrms.marco.be02marbox.domain.user.exception.InvalidEmailException;
import prgrms.marco.be02marbox.domain.user.repository.UserRepository;

@DataJpaTest
@Import({UserService.class})
class UserServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

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
	@DisplayName("사용 생성 실패 - 이메일 중복")
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
	@DisplayName("사용 생성 실패 - 이름 중복")
	void testSaveFailBecauseDuplicateName() {
		//given
		String duplicateName = "pang";
		User user = new User(
			"pang@mail.com",
			"1234",
			duplicateName,
			Role.ROLE_CUSTOMER);
		userRepository.save(user);

		//when then
		assertThatThrownBy(() -> userService.create(
			"bang@mail.com",
			"1234",
			duplicateName,
			Role.ROLE_CUSTOMER))
			.isInstanceOf(DuplicateNameException.class)
			.hasMessageContaining("이미 존재하는 이름입니다.");
	}

	@Test
	@DisplayName("사용자 로그인 성공")
	void testLoginSuccess() {
		//given
		User user = new User(
			"pang@mail.com",
			"1234",
			"pang",
			Role.ROLE_ADMIN);
		User savedUser = userRepository.save(user);

		//when
		ResponseLoginUser responseLoginUser = userService.login(savedUser.getEmail(), savedUser.getPassword());

		//then
		assertAll(
			() -> assertThat(responseLoginUser.name()).isEqualTo(savedUser.getName()),
			() -> assertThat(responseLoginUser.role()).isEqualTo(savedUser.getRoleName())
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
			.hasMessageContaining("비밀번호가 틀렸습니다.");
	}
}
