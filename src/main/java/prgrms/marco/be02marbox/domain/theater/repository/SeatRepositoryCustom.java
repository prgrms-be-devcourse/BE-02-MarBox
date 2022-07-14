package prgrms.marco.be02marbox.domain.theater.repository;

import java.util.List;

import prgrms.marco.be02marbox.domain.theater.Seat;

public interface SeatRepositoryCustom {

	List<Seat> findByTheaterRoomIdAndIdIn(Long theaterRoomId, List<Long> reservedSeatIdList);

	List<Seat> findByTheaterRoomIdAndIdNotIn(Long theaterRoomId, List<Long> reservedSeatIdList);

	List<Seat> findByIdIn(List<Long> seatIdList);
}
