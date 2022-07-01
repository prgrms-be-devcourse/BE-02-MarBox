package prgrms.marco.be02marbox.domain.reservation.service.utils;

import org.springframework.stereotype.Component;

import prgrms.marco.be02marbox.domain.reservation.Ticket;
import prgrms.marco.be02marbox.domain.reservation.dto.RequestCreateTicket;
import prgrms.marco.be02marbox.domain.reservation.dto.ResponseFindTicket;

@Component
public class TicketConverter {

	public ResponseFindTicket convertFromTicketToResponseFindTicket(Ticket ticket) {
		return new ResponseFindTicket(ticket.getUser(), ticket.getSchedule(), ticket.getReservedAt());
	}

	public Ticket convertFromRequestCreateTicketToTicket(RequestCreateTicket request) {
		return new Ticket(request.user(), request.schedule(), request.reservedAt());
	}
}
