package prgrms.marco.be02marbox.domain.reservation.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import prgrms.marco.be02marbox.domain.reservation.dto.RequestReservation;
import prgrms.marco.be02marbox.domain.reservation.service.ReservationService;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

	private final ReservationService reservationService;

	public ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@PostMapping
	public ResponseEntity<Void> reservationTicket(@Valid @RequestBody RequestReservation requestReservation) {
		Long ticketId = reservationService.reservation(requestReservation);
		return ResponseEntity.created(URI.create("/tickets/" + ticketId)).build();
	}

}
