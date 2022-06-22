package prgrms.marco.be02marbox.domain.user.service;

import static prgrms.marco.be02marbox.domain.user.exception.Message.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.user.Role;
import prgrms.marco.be02marbox.domain.user.User;
import prgrms.marco.be02marbox.domain.user.exception.DuplicateEmailException;
import prgrms.marco.be02marbox.domain.user.exception.DuplicateNameException;
import prgrms.marco.be02marbox.domain.user.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

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
}
