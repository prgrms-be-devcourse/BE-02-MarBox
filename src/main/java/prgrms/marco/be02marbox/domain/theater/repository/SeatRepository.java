package prgrms.marco.be02marbox.domain.theater.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import prgrms.marco.be02marbox.domain.theater.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {
	List<Seat> findByTheaterRoomId(Long theaterRoomId);

	@Query("select s from Seat s where s.theaterRoom.id=:theaterRoomId and s.id in (:reservedSeatIdList)")
	List<Seat> findByTheaterRoomIdAndIdIn(Long theaterRoomId, List<Long> reservedSeatIdList);

	@Query("select s from Seat s where s.theaterRoom.id=:theaterRoomId and s.id not in (:reservedSeatIdList)")
	List<Seat> findByTheaterRoomIdAndIdNotIn(Long theaterRoomId, List<Long> reservedSeatIdList);
}
