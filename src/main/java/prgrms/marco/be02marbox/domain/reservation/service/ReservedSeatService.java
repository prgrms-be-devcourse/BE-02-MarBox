package prgrms.marco.be02marbox.domain.reservation.service;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.reservation.ReservedSeat;
import prgrms.marco.be02marbox.domain.reservation.repository.ReservedSeatRepository;

@Service
@Transactional(readOnly = true)
public class ReservedSeatService {

	private final ReservedSeatRepository reservedSeatRepository;

	public ReservedSeatService(ReservedSeatRepository reservedSeatRepository) {
		this.reservedSeatRepository = reservedSeatRepository;
	}

	/**
	 * 스케줄 별 예약좌석 조회
	 *
	 * @param scheduleId 스케줄 id
	 * @return 예약(되어있는) 좌석의 id 리스트
	 */
	public Set<Long> findReservedIdListByScheduleId(Long scheduleId) {
		return reservedSeatRepository.searchByScheduleIdStartsWith(scheduleId).stream()
			.map(reservedSeat -> reservedSeat.getSeat().getId())
			.collect(toSet());
	}

	@Transactional
	public void saveAll(List<ReservedSeat> selectedSeats) {
		reservedSeatRepository.saveAll(selectedSeats);
	}

}
