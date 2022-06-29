package prgrms.marco.be02marbox.domain.theater.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import prgrms.marco.be02marbox.domain.theater.TheaterRoom;

public interface TheaterRoomRepository extends JpaRepository<TheaterRoom, Long> {

	Set<TheaterRoom> findAllByTheaterId(Long theaterId);
}
