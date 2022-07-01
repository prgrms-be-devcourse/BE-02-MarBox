package prgrms.marco.be02marbox.domain.reservation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import prgrms.marco.be02marbox.domain.reservation.service.ReservedSeatService;
import prgrms.marco.be02marbox.domain.theater.dto.ResponseFindSeat;

@RestController
@RequestMapping("/reserved-seat")
public class ReservedSeatController {

	private final ReservedSeatService reservedSeatService;

	public ReservedSeatController(ReservedSeatService reservedSeatService) {
		this.reservedSeatService = reservedSeatService;
	}

	@GetMapping("/{scheduleId}")
	public ResponseEntity<List<ResponseFindSeat>> findByScheduleId(@PathVariable Long scheduleId) {
		return ResponseEntity.ok(reservedSeatService.findByScheduleId(scheduleId));
	}

	@GetMapping("/possible/{scheduleId}")
	public ResponseEntity<List<ResponseFindSeat>> findReservePossibleSeats(@PathVariable Long scheduleId) {
		return ResponseEntity.ok(reservedSeatService.findReservePossibleSeats(scheduleId));
	}
}
