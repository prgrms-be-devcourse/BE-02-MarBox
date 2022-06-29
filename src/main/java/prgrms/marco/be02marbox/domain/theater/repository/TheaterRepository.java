package prgrms.marco.be02marbox.domain.theater.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import prgrms.marco.be02marbox.domain.theater.Region;
import prgrms.marco.be02marbox.domain.theater.Theater;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
	List<Theater> findAllByRegion(Region region);

	Optional<Theater> findByName(String name);
}
