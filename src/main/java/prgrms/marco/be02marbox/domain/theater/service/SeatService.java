package prgrms.marco.be02marbox.domain.theater.service;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;

import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindSeat;
import prgrms.marco.be02marbox.domain.theater.repository.SeatRepository;
import prgrms.marco.be02marbox.domain.theater.service.utils.SeatConverter;

@Service
public class SeatService {

	private final SeatRepository seatRepository;
	private final SeatConverter seatConverter;

	public SeatService(SeatRepository seatRepository, SeatConverter seatConverter) {
		this.seatRepository = seatRepository;
		this.seatConverter = seatConverter;
	}

	/**
	 * 예약 가능한 좌석 정보 조회
	 *
	 * @param theaterRoomId 상영관 ID
	 * @param reservedSeatIdList 예약 된 좌석리스트
	 * @return 예약 가능한 좌석 리스트
	 */
	public List<ResponseFindSeat> findRemainSeats(Long theaterRoomId, List<Long> reservedSeatIdList) {
		return seatRepository.findByTheaterRoomIdAndIdNotIn(theaterRoomId, reservedSeatIdList).stream()
			.map(seatConverter::convertFromSeatToResponseFindSeat)
			.collect(toList());
	}
}
