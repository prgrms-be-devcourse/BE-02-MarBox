package prgrms.marco.be02marbox.domain.user.service;

import static prgrms.marco.be02marbox.domain.exception.custom.Message.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.exception.custom.user.DuplicateEmailException;
import prgrms.marco.be02marbox.domain.exception.custom.user.DuplicateNameException;
import prgrms.marco.be02marbox.domain.exception.custom.user.InvalidEmailException;
import prgrms.marco.be02marbox.domain.user.Role;
import prgrms.marco.be02marbox.domain.user.User;
import prgrms.marco.be02marbox.domain.user.dto.ResponseLoginUser;
import prgrms.marco.be02marbox.domain.user.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * 사용자 정보를 받아, User 엔티티를 만들고 DB에 저장한다.
	 * @param email
	 * @param password
	 * @param name
	 * @param role
	 * @return 사용자 아이디
	 * @throws DuplicateEmailException - 입력 받은 이메일이 DB에 존재하는 경우
	 * @throws DuplicateNameException - 입력 받은 이름이 DB에 존재하는 경우
	 */
	@Transactional
	public Long create(String email, String password, String name, Role role) {
		userRepository.findByEmail(email)
			.ifPresent(user -> {
				throw new DuplicateEmailException(DUPLICATE_EMAIL_EXP_MSG);
			});

		userRepository.findByName(name)
			.ifPresent(user -> {
				throw new DuplicateNameException(DUPLICATE_NAME_EXP_MSG);
			});

		User user = new User(email, password, name, role);
		User savedUser = userRepository.save(user);
		return savedUser.getId();
	}

	/**
	 * 이메일과 비밀번호를 받아, 존재하는 사용자인지 확인한다.
	 * @param email
	 * @param password
	 * @return ResponseLoginUser
	 * @throws InvalidEmailException 이메일이 DB에 존재하지 않는 경우
	 * @throws org.springframework.security.authentication.BadCredentialsException 비밀번호가 틀린 경우
	 */
	public ResponseLoginUser login(String email, String password) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new InvalidEmailException(INVALID_EMAIL_EXP_MSG));

		user.checkPassword(password);

		return new ResponseLoginUser(user.getName(), user.getRoleName());
	}
}
