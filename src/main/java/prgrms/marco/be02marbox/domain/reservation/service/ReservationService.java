package prgrms.marco.be02marbox.domain.reservation.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import prgrms.marco.be02marbox.domain.reservation.dto.ResponseFindReservedSeat;
import prgrms.marco.be02marbox.domain.theater.Schedule;
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
	 * @return 예약 좌석리스트
	 */
	public List<ResponseFindReservedSeat> findReservePossibleSeatList(Long scheduleId) {
		Schedule schedule = scheduleService.findById(scheduleId);

		Set<Long> reservedSeatIdList = reservedSeatService.findReservedIdListByScheduleId(schedule.getId());
		return seatService.findAvailableSeatList(schedule.getTheaterRoom().getId(), reservedSeatIdList);
	}

	// /**
	//  *
	//  */
	// @Transactional
	// public List<ResponseReservation> reservation(RequestReservation requestReservation) {
	// 	//결제
	// 	//티켓 저장
	// }
}
