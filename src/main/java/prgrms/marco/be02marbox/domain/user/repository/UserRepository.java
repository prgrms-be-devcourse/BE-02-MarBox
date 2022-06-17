package prgrms.marco.be02marbox.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import prgrms.marco.be02marbox.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
