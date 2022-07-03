package prgrms.marco.be02marbox.domain.reservation.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindSeat;
import prgrms.marco.be02marbox.domain.theater.service.ScheduleService;
import prgrms.marco.be02marbox.domain.theater.service.SeatService;

@Service
@Transactional(readOnly = true)
public class ReservationService {

	private final ReservedSeatService reservedSeatService;
	private final SeatService seatService;
	private final ScheduleService scheduleService;

	public ReservationService(ReservedSeatService reservedSeatService,
		SeatService seatService, ScheduleService scheduleService) {
		this.reservedSeatService = reservedSeatService;
		this.seatService = seatService;
		this.scheduleService = scheduleService;
	}

	/**
	 * 스케줄 별 예약좌석 조회
	 *
	 * @param scheduleId 스케줄 id
	 * @return 예약(되어있는) 좌석의 id 리스트
	 */
	public List<ResponseFindSeat> findReservePossibleSeatList(Long scheduleId) {
		Schedule schedule = scheduleService.findById(scheduleId);

		List<Long> reservedSeatIdList = reservedSeatService.findReservedIdListByScheduleId(schedule.getId());
		return seatService.findRemainSeats(schedule.getTheaterRoom().getId(), reservedSeatIdList);
	}
}
