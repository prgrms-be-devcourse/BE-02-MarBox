package prgrms.marco.be02marbox.domain.reservation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import prgrms.marco.be02marbox.domain.reservation.Account;
import prgrms.marco.be02marbox.domain.user.User;

public interface AccountRepository extends JpaRepository<Account, Long> {
	Optional<Account> findByUser(User user);
}
