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

	/**
	 * 예약 가능한 좌석 정보 조회
	 *
	 * @param theaterRoomId 상영관 ID
	 * @param reservedSeatIdList 예약 된 좌석리스트
	 * @return 예약 가능한 좌석 리스트
	 */
	public List<Seat> findRemainSeats(Long theaterRoomId, List<Long> reservedSeatIdList) {
		return seatRepository.findByTheaterRoomIdAndIdNotIn(theaterRoomId, reservedSeatIdList);
	}
}
