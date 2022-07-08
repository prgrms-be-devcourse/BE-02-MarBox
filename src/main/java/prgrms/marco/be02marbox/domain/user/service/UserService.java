package prgrms.marco.be02marbox.domain.user.service;

import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.exception.custom.user.DuplicateEmailException;
import prgrms.marco.be02marbox.domain.exception.custom.user.InvalidEmailException;
import prgrms.marco.be02marbox.domain.user.Role;
import prgrms.marco.be02marbox.domain.user.User;
import prgrms.marco.be02marbox.domain.user.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * 사용자 정보를 받아, User 엔티티를 만들고 DB에 저장한다.
	 * @param email
	 * @param password
	 * @param name
	 * @param role
	 * @return 사용자 아이디
	 * @throws DuplicateEmailException - 입력 받은 이메일이 DB에 존재하는 경우
	 */
	@Transactional
	public Long create(String email, String password, String name, Role role) {
		userRepository.findByEmail(email)
			.ifPresent(user -> {
				throw new DuplicateEmailException(DUPLICATE_EMAIL_EXP_MSG);
			});

		User user = new User(email, passwordEncoder.encode(password), name, role);
		User savedUser = userRepository.save(user);
		return savedUser.getId();
	}

	/**
	 * 이메일과 비밀번호를 받아, 사용자를 인증한다.
	 * @param email
	 * @param password
	 * @return User
	 * @throws @throws InvalidEmailException 이메일이 DB에 존재하지 않는 경우
	 * @throws org.springframework.security.authentication.BadCredentialsException 비밀번호가 틀린 경우
	 */
	public User login(String email, String password) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new InvalidEmailException(INVALID_EMAIL_EXP_MSG));

		user.checkPassword(passwordEncoder, password);

		return user;
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new InvalidEmailException(INVALID_EMAIL_EXP_MSG));
	}
}
