package prgrms.marco.be02marbox.domain.theater.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import prgrms.marco.be02marbox.domain.theater.TheaterRoom;

public interface TheaterRoomRepository extends JpaRepository<TheaterRoom, Long> {

	List<TheaterRoom> findAllByTheaterId(Long theaterId);
}
