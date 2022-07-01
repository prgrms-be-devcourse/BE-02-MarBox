package prgrms.marco.be02marbox.domain.reservation.service;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.reservation.repository.ReservedSeatRepository;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindSeat;
import prgrms.marco.be02marbox.domain.theater.service.ScheduleService;
import prgrms.marco.be02marbox.domain.theater.service.SeatService;
import prgrms.marco.be02marbox.domain.theater.service.utils.SeatConverter;

@Service
@Transactional(readOnly = true)
public class ReservedSeatService {

	private final ReservedSeatRepository reservedSeatRepository;
	private final SeatConverter seatConverter;
	private final SeatService seatService;
	private final ScheduleService scheduleService;

	public ReservedSeatService(ReservedSeatRepository reservedSeatRepository, SeatConverter seatConverter,
		SeatService seatService, ScheduleService scheduleService) {
		this.reservedSeatRepository = reservedSeatRepository;
		this.seatConverter = seatConverter;
		this.seatService = seatService;
		this.scheduleService = scheduleService;
	}

	/**
	 * 스케줄 별 예약좌석 조회
	 *
	 * @param scheduleId 스케줄 id
	 * @return 예약(되어있는) 좌석 리스트
	 */
	public List<ResponseFindSeat> findByScheduleId(Long scheduleId) {
		return reservedSeatRepository.searchByScheduleIdStartsWith(scheduleId).stream()
			.map((reservedSeat -> seatConverter.convertFromSeatToResponseFindSeat(reservedSeat.getSeat())))
			.collect(toList());
	}

	/**
	 * 스케줄 별 예약 가능한 좌석 조회
	 *
	 * @param scheduleId 스케줄 ID
	 * @return 예약(가능한) 좌석 리스트
	 */
	public List<ResponseFindSeat> findReservePossibleSeats(Long scheduleId) {
		Schedule schedule = scheduleService.findById(scheduleId);
		List<Long> reservedSeatIdList = reservedSeatRepository.searchByScheduleIdStartsWith(scheduleId).stream()
			.map((reservedSeat) -> reservedSeat.getSeat().getId())
			.collect(toList());

		return seatService.findRemainSeats(schedule.getTheaterRoom().getId(), reservedSeatIdList).stream()
			.map(seatConverter::convertFromSeatToResponseFindSeat)
			.collect(toList());
	}
}
