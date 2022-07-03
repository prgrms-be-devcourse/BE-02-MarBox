package prgrms.marco.be02marbox.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import prgrms.marco.be02marbox.domain.user.RefreshToken;
import prgrms.marco.be02marbox.domain.user.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByUser(User user);

	@Query("SELECT rt FROM RefreshToken rt JOIN FETCH rt.user WHERE rt.token=:refreshToken")
	Optional<RefreshToken> findByToken(@Param("refreshToken") String refreshToken);
}
