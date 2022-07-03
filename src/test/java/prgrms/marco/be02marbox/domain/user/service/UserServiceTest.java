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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import prgrms.marco.be02marbox.domain.exception.custom.user.DuplicateEmailException;
import prgrms.marco.be02marbox.domain.exception.custom.user.InvalidEmailException;
import prgrms.marco.be02marbox.domain.user.Role;
import prgrms.marco.be02marbox.domain.user.User;
import prgrms.marco.be02marbox.domain.user.repository.UserRepository;

@DataJpaTest
@Import({UserService.class, BCryptPasswordEncoder.class})
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
	@DisplayName("findByEmail 성공")
	void testFindByEmailSuccess() {
		//given
		User user = new User(
			"pang@mail.com",
			"1234",
			"pang",
			Role.ROLE_CUSTOMER);
		User savedUser = userRepository.save(user);

		//when
		User retrievedUser = userService.findByEmail(user.getEmail());

		//then
		assertThat(retrievedUser.getId()).isEqualTo(savedUser.getId());
	}

	@Test
	@DisplayName("findByEmail 실패 - 존재하지 않는 이메일")
	void testFindByEmailFailBecauseInvalidEmail() {
		//given when then
		assertThatThrownBy(() -> userService.findByEmail("invalid@mail.com"))
			.isInstanceOf(InvalidEmailException.class)
			.hasMessageContaining(INVALID_EMAIL_EXP_MSG.getMessage());
	}
}
