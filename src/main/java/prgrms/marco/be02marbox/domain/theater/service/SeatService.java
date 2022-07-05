package prgrms.marco.be02marbox.domain.theater.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import prgrms.marco.be02marbox.domain.reservation.dto.ResponseFindReservedSeat;
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
	 * @return 예약 가능 좌석 정보
	 */
	public List<ResponseFindReservedSeat> findAvailableSeatList(Long theaterRoomId, Set<Long> reservedSeatIdList) {
		List<Seat> allSeat = seatRepository.findByTheaterRoomId(theaterRoomId);
		List<ResponseFindReservedSeat> seatList = new ArrayList<>();

		allSeat.forEach(seat -> {
			boolean reserved = reservedSeatIdList.contains(seat.getId());
			seatList.add(new ResponseFindReservedSeat(seat.getRow(), seat.getColumn(), reserved));
		});
		return seatList;
	}
}
