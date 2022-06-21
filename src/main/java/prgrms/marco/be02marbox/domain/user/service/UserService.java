package prgrms.marco.be02marbox.domain.user.service;

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
				throw new DuplicateEmailException("이미 존재하는 이메일 입니다.");
			});

		userRepository.findByName(name)
			.ifPresent(user -> {
				throw new DuplicateNameException("이미 존재하는 이름입니다.");
			});

		User user = new User(email, password, name, role);
		User savedUser = userRepository.save(user);
		return savedUser.getId();
	}
}
