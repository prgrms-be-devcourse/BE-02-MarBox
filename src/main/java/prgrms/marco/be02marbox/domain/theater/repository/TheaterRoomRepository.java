package prgrms.marco.be02marbox.domain.theater.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import prgrms.marco.be02marbox.domain.theater.TheaterRoom;

public interface TheaterRoomRepository extends JpaRepository<TheaterRoom, Long> {

	Set<TheaterRoom> findAllByTheaterId(Long theaterId);

	@Query("SELECT tr FROM TheaterRoom tr JOIN FETCH tr.theater")
	List<TheaterRoom> findAll();
}
