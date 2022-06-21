package prgrms.marco.be02marbox.domain.theater.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import prgrms.marco.be02marbox.domain.theater.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {
	List<Seat> findByTheaterRoomId(Long theaterRoomId);
}
