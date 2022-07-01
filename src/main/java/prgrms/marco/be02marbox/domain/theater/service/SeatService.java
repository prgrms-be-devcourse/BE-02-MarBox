package prgrms.marco.be02marbox.domain.theater.service;

import java.util.List;

import org.springframework.stereotype.Service;

import prgrms.marco.be02marbox.domain.theater.Seat;
import prgrms.marco.be02marbox.domain.theater.repository.SeatRepository;

@Service
public class SeatService {

	private final SeatRepository seatRepository;

	public SeatService(SeatRepository seatRepository) {
		this.seatRepository = seatRepository;
	}

	public List<Seat> findRemainSeats(Long theaterRoomId, List<Long> reservedSeatIdList) {
		return seatRepository.findByTheaterRoomIdAndIdNotIn(theaterRoomId, reservedSeatIdList);
	}
}
