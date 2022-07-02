package prgrms.marco.be02marbox.domain.reservation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import prgrms.marco.be02marbox.domain.reservation.service.ReservedSeatService;
import prgrms.marco.be02marbox.domain.theater.Schedule;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindSeat;
import prgrms.marco.be02marbox.domain.theater.service.ScheduleService;
import prgrms.marco.be02marbox.domain.theater.service.SeatService;

@RestController
@RequestMapping("/reserved-seat")
public class ReservedSeatController {

	private final ReservedSeatService reservedSeatService;
	private final SeatService seatService;
	private final ScheduleService scheduleService;

	public ReservedSeatController(ReservedSeatService reservedSeatService,
		SeatService seatService, ScheduleService scheduleService) {
		this.reservedSeatService = reservedSeatService;
		this.seatService = seatService;
		this.scheduleService = scheduleService;
	}

	@GetMapping("/{scheduleId}")
	public ResponseEntity<List<ResponseFindSeat>> findByScheduleId(@PathVariable Long scheduleId) {
		return ResponseEntity.ok(reservedSeatService.findByScheduleId(scheduleId));
	}

	@GetMapping("/{scheduleId}/possible")
	public ResponseEntity<List<ResponseFindSeat>> findReservePossibleSeats(@PathVariable Long scheduleId) {
		Schedule schedule = scheduleService.findById(scheduleId);

		List<Long> reservedSeatIdList = reservedSeatService.findReservedIdListByScheduleId(schedule.getId());
		return ResponseEntity.ok(seatService.findRemainSeats(schedule.getTheaterRoom().getId(), reservedSeatIdList));
	}
}
