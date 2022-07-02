package prgrms.marco.be02marbox.domain.reservation.service;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.reservation.repository.ReservedSeatRepository;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindSeat;
import prgrms.marco.be02marbox.domain.theater.service.utils.SeatConverter;

@Service
@Transactional(readOnly = true)
public class ReservedSeatService {

	private final ReservedSeatRepository reservedSeatRepository;
	private final SeatConverter seatConverter;

	public ReservedSeatService(ReservedSeatRepository reservedSeatRepository, SeatConverter seatConverter) {
		this.reservedSeatRepository = reservedSeatRepository;
		this.seatConverter = seatConverter;
	}

	/**
	 * 스케줄 별 예약좌석 조회
	 *
	 * @param scheduleId 스케줄 id
	 * @return 예약 좌석 리스트
	 */
	public List<ResponseFindSeat> findByScheduleId(Long scheduleId) {
		return reservedSeatRepository.searchByScheduleIdStartsWith(scheduleId).stream()
			.map((reservedSeat -> seatConverter.convertFromSeatToResponseFindSeat(reservedSeat.getSeat())))
			.collect(toList());
	}
}
