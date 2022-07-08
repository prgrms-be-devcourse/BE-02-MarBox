package prgrms.marco.be02marbox.domain.reservation.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import prgrms.marco.be02marbox.domain.reservation.dto.RequestCreateTicket;
import prgrms.marco.be02marbox.domain.reservation.dto.ResponseFindTicket;
import prgrms.marco.be02marbox.domain.reservation.service.TicketService;

@RestController
@RequestMapping("/tickets")
public class TicketController {

	private final TicketService ticketService;

	public TicketController(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@PostMapping
	public ResponseEntity<ResponseFindTicket> saveTicket(@RequestBody @Valid RequestCreateTicket request) {
		Long savedTicketId = ticketService.createTicket(request);
		return ResponseEntity.created(URI.create("/tickets/" + savedTicketId))
			.body(ticketService.findTicket(savedTicketId));
	}

	@GetMapping("/{ticketId}")
	public ResponseEntity<ResponseFindTicket> getTicket(@PathVariable Long ticketId) {
		return ResponseEntity.ok().body(ticketService.findTicket(ticketId));
	}

	@GetMapping
	public ResponseEntity<List<ResponseFindTicket>> getAllTicket() {
		return ResponseEntity.ok().body(ticketService.findTickets());
	}

	@GetMapping(params = "user-id")
	public ResponseEntity<List<ResponseFindTicket>> getTicketsOfUser(
		@RequestParam(value = "user-id", required = true) Long userId) {
		return ResponseEntity.ok().body(ticketService.findTicketsOfUser(userId));
	}

	@GetMapping(params = "schedule_id")
	public ResponseEntity<List<ResponseFindTicket>> getTicketsOfSchedule(
		@RequestParam(value = "schedule_id", required = true) Long scheduleId) {
		return ResponseEntity.ok().body(ticketService.findTicketsOfSchedule(scheduleId));
	}

	@GetMapping("/valid")
	public ResponseEntity<List<ResponseFindTicket>> getValidTicketsOfUser(
		@RequestParam(value = "user-id") Long userId) {
		return ResponseEntity.ok().body(ticketService.findValidTicketsOfUser(userId));
	}
}
