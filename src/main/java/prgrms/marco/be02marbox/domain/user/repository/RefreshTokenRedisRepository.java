package prgrms.marco.be02marbox.domain.user.repository;

import org.springframework.data.repository.CrudRepository;

import prgrms.marco.be02marbox.domain.user.RefreshToken;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
