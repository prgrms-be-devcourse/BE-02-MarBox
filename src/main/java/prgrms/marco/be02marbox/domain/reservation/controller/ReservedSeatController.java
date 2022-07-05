package prgrms.marco.be02marbox.domain.reservation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import prgrms.marco.be02marbox.domain.reservation.dto.ResponseFindReservedSeat;
import prgrms.marco.be02marbox.domain.reservation.service.ReservationService;

@RestController
@RequestMapping("/reserved-seat")
public class ReservedSeatController {

	private final ReservationService reservationService;

	public ReservedSeatController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@GetMapping("/{scheduleId}")
	public ResponseEntity<List<ResponseFindReservedSeat>> findReservePossibleSeats(@PathVariable Long scheduleId) {
		return ResponseEntity.ok(reservationService.findReservePossibleSeatList(scheduleId));
	}
}
